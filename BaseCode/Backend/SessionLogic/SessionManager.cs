using BusinessLogic;
using DataAccessInterface;
using Domain;
using SessionInterface;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;

namespace SessionLogic
{
    public class SessionManager : ISessionManager
    {
        private readonly IDataAccess<AppUser> userDataAccess;
        private static readonly IDictionary<string, string> TokenRepository = new Dictionary<string, string>();

        public SessionManager(IDataAccess<AppUser> userDA)
        {
            userDataAccess = userDA;
        }

        public Guid? CreateToken(string email, string password)
        {
            try
            {
                var userWithEmail = userDataAccess.GetByCondition(a => a.MainEmail == email);
                if (userWithEmail == null)
                {
                    return null;
                }
                if (!IsPasswordCorrect(userWithEmail, password))
                {
                    return null;
                }
                foreach (var pair in from pair in TokenRepository
                                     where pair.Value.Equals(email)
                                     select pair)
                {
                    return new Guid(pair.Key);
                }
                var token = Guid.NewGuid();
                TokenRepository.Add(token.ToString(), userWithEmail.Id.ToString());
                return token;
            }
            catch (DataAccessException e)
            {
                throw new SessionException(e.Message);
            }
        }

        private bool IsPasswordCorrect(AppUser userWithEmail, string passwordToCheck)
        {
            return CryptoHelper.IsPasswordValid(passwordToCheck, userWithEmail.Password);
        }

        public string GetApiKey()
        {
            var key = ConfigurationManager.AppSettings["ApiKey"];
            return key;
        }

        public bool IsValidToken(string token)
        {
            return TokenRepository.ContainsKey(token);
        }

        public Guid GetUser(string token)
        {
            return new Guid(TokenRepository[token]);
        }
    }
}

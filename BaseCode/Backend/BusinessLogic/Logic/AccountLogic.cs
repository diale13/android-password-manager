using BusinessLogicInterface;
using DataAccessInterface;
using Domain;
using System;
using System.Collections.Generic;
using System.Linq;

namespace BusinessLogic
{
    public class AccountLogic : IAccountLogic
    {
        private readonly IDataAccess<AppUser> userRepo;

        public AccountLogic(IDataAccess<AppUser> repo)
        {
            userRepo = repo;
        }

        public List<Account> GetAccountsByUser(Guid userId)
        {
            try
            {
                return userRepo.Get(userId).Accounts;
            }
            catch (DomainException e)
            {
                throw new BusinessLogicException(e.Message);
            }
            catch (DataAccessException e)
            {
                throw new BusinessLogicException(e.Message);
            }
        }

        public void UpdateAccounts(Guid userId, List<Account> accountsToUpdate)
        {
            try
            {
                var user = userRepo.Get(userId);
                foreach (var (account, accountToUpdate) in from account in user.Accounts
                                                           from accountToUpdate in accountsToUpdate
                                                           where account.Equals(accountToUpdate)
                                                           select (account, accountToUpdate))
                {
                    account.EmailHolder = accountToUpdate.EmailHolder;
                }
                user.Validate();
                userRepo.Update(user);               
            }
            catch (DomainException e)
            {
                throw new BusinessLogicException(e.Message);
            }
            catch (DataAccessException e)
            {
                throw new BusinessLogicException(e.Message);
            }
        }
    }
}

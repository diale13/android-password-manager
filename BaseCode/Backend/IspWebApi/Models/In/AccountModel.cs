using Domain;
using System;


namespace IspWebApi.Models
{

    /// <summary>Represents the one of accounts for a given user</summary>
    public class AccountModel
    {
        public string UserName { get; set; }
        public string Password { get; set; }
        public string Site { get; set; }
        public string Email { get; set; }

        public Account ToEntity(Guid ownerId)
        {
            return new Account()
            {
                UserName = this.UserName,
                Password = this.Password,
                Site = this.Site,
                EmailHolder = new EmailHolder(ownerId, this.Email)
            };
        }
    }
}

using Domain;
using System;
using System.Collections.Generic;
using System.Linq;

namespace IspWebApi.Models
{
    public class UserModelIn
    {
        /// <summary>The Firebase token for the device</summary>
        public string DeviceId { get; set; }
        public List<AccountModel> AccountModels { get; set; }
        public string MainEmail { get; set; }
        public string Password { get; set; }

        public AppUser ToEntity()
        {

            var entityUser = new AppUser()
            {
                Id = new Guid(),
                Password = this.Password,
                MainEmail = this.MainEmail,
                DeviceId = this.DeviceId,
                Accounts = new List<Account>()
            };
            entityUser.Accounts.AddRange(from account in AccountModels
                                         select account.ToEntity(entityUser.Id));
            return entityUser;
        }
    }
}

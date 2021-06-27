using Domain.Exceptions;
using System;
using System.Collections.Generic;
using System.Text.RegularExpressions;

namespace Domain
{
    public class AppUser
    {
        public string DeviceId { get; set; }
        public Guid Id { get; set; }
        public string MainEmail { get; set; }
        public string Password { get; set; }
        public List<Account> Accounts { get; set; }
     

        public void Validate()
        {
            //TODO validate device id
            AtributesNotEmpty();
            if (!IsValidMail())
            {
                throw new DomainException(ExceptionMessages.EMAIL_BAD_FORMAT);
            }
            foreach (var account in Accounts)
            {
                account.Validate();
            }
        }

        private bool IsValidMail()
        {
            Regex re = new Regex(@"^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$",
                 RegexOptions.IgnoreCase);
            return re.IsMatch(MainEmail);
        }

        private void AtributesNotEmpty()
        {
           
            if(String.IsNullOrEmpty(Password) || String.IsNullOrEmpty(this.MainEmail))
            {
                throw new DomainException(ExceptionMessages.EMPTY_ATRIBUTE);
            }
            if (String.IsNullOrEmpty(DeviceId))
            {
                throw new DomainException(ExceptionMessages.EMPTY_ATRIBUTE);
            }
        }

        public override bool Equals(object obj)
        {
            return obj is AppUser user &&
                   DeviceId == user.DeviceId &&
                   Id.Equals(user.Id) &&
                   MainEmail == user.MainEmail &&
                   Password == user.Password;
        }
    }
}

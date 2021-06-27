using Domain.Exceptions;
using System;

namespace Domain
{
    public class Account
    {
        public Guid Id { get; set; }
        public string UserName { get; set; }
        public string Password { get; set; }
        public string Site { get; set; }
        public EmailHolder EmailHolder { get; set; }


        public void Validate()
        {
            ValidateNotNullOrEmptyAtributes();
            EmailHolder.Validate();
        }

        private void ValidateNotNullOrEmptyAtributes()
        {
            var usernameOk = !String.IsNullOrEmpty(UserName);
            var passwordOk = !String.IsNullOrEmpty(Password);
            var siteOk = !String.IsNullOrEmpty(Site);
            if(!(usernameOk && passwordOk && siteOk))
            {
                throw new DomainException(ExceptionMessages.EMPTY_ATRIBUTE);
            }
        }

        public bool HasLeaks()
        {
            return this.EmailHolder.WasLeaked;
        }

        public override bool Equals(object obj)
        {
            return obj is Account account &&
                   Id.Equals(account.Id);
        }
    }
}

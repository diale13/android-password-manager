using Domain.Exceptions;
using System;
using System.Collections.Generic;
using System.Text.RegularExpressions;

namespace Domain
{
    public class EmailHolder
    {
        public Guid Id { get; set; }
        public string Email { get; set; }
        public Guid OwnerUserId { get; set; }
        public bool WasLeaked { get; set; }
        public List<LeakedSite> LeakedSites { get; set; }
    
        public EmailHolder()
        {

        }

        public EmailHolder(Guid ownerId, string email)
        {
            Id = new Guid();
            WasLeaked = false;
            LeakedSites = new List<LeakedSite>();
            Email = email;
            OwnerUserId = ownerId;
        }

        public void Validate()
        {
            ValidateLeakedSites();
            ValidateEmail();
        }

        private void ValidateEmail()
        {
            Regex re = new Regex(@"^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$",
                  RegexOptions.IgnoreCase);
            if (!re.IsMatch(this.Email))
            {
                throw new DomainException(ExceptionMessages.EMAIL_BAD_FORMAT);
            }
        }

       
        private void ValidateLeakedSites()
        {
            foreach (var site in LeakedSites)
            {
                site.Validate();
            }
        }

        public override bool Equals(object obj)
        {
            return obj is EmailHolder holder &&
                   Email == holder.Email &&
                   OwnerUserId.Equals(holder.OwnerUserId);
        }
    }
}

using Domain.Exceptions;
using System;

namespace Domain
{
    public class LeakedSite
    {
        public LeakedSite() { }

        public LeakedSite(DummyLeak leak)
        {
            Id = new Guid();
            Site = leak.Site;
            NotificationSent = false;
            Url = leak.Url;
        }
        public Guid Id { get; set; }
        public string Site { get; set; }
        public string Url { get; set; }
        public bool NotificationSent { get; set; }

        public void Validate()
        {
            ValidateUrl();
            ValidateName();
        }

        private void ValidateName()
        {
            if (String.IsNullOrEmpty(Site)) throw new DomainException(ExceptionMessages.EMPTY_ATRIBUTE);
        }

        private void ValidateUrl()
        {
            bool isUri = Uri.IsWellFormedUriString(Url, UriKind.RelativeOrAbsolute);
            if (!isUri) throw new DomainException(ExceptionMessages.BAD_URI);
        }

    }
}

using Domain;
using System;

namespace IspWebApi.Models
{
    public class DummyModelIn
    {
        public string Site { get; set; }
        public string Url { get; set; }
        public string Email { get; set; }

        public DummyLeak ToEntity()
        {
            var leak = new DummyLeak()
            {
                Id = new Guid(),
                Email = this.Email,
                Site = this.Site,
                Url = this.Url
            };
            return leak;
        }

        public bool Validate()
        {
            return ValidateUrl() && ValidateName();
        }

        private bool ValidateName()
        {
            return !String.IsNullOrEmpty(Site);
        }

        private bool ValidateUrl()
        {
            return Uri.IsWellFormedUriString(Url, UriKind.RelativeOrAbsolute);
        }

    }
}

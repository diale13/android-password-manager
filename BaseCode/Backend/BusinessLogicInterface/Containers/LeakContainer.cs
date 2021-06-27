using System;
using System.Collections.Generic;
using System.Text;

namespace BusinessLogicInterface
{
    public class LeakContainer
    {
        public string LeakedSiteName { get; set; }
        public string Url { get; set; }
        public string AsociatedEmail { get; set; }

        public override bool Equals(object obj)
        {
            return obj is LeakContainer container &&
                   LeakedSiteName == container.LeakedSiteName &&
                   Url == container.Url &&
                   AsociatedEmail == container.AsociatedEmail;
        }

        public override int GetHashCode()
        {
            return HashCode.Combine(LeakedSiteName, Url, AsociatedEmail);
        }
    }
}

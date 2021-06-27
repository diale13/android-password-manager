using System;
using System.Collections.Generic;
using System.Text;

namespace Domain
{
    public class Message
    {
        public string[] registration_ids { get; set; }
        public Notification notification { get; set; }
        public string priority { get; set; }
    }
}

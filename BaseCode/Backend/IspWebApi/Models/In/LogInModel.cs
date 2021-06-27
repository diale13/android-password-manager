using System;
using System.Text.RegularExpressions;

namespace IspWebApi.Models
{
    public class LogInModel
    {
        public string Email { get; set; }
        public string Password { get; set; }

        public LogInModel()
        {
            Email = "";
            Password = "";
        }

        public bool IsValid()
        {
            return AtributesNotEmpty() && IsValidMail();
        }

        private bool IsValidMail()
        {
            Regex re = new Regex(@"^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$",
                 RegexOptions.IgnoreCase);
            return re.IsMatch(Email);
        }

        private bool AtributesNotEmpty()
        {
            var passwordToValidate = this.Password;
            var emailToValidate = this.Email;
            return !(String.IsNullOrEmpty(passwordToValidate) || String.IsNullOrEmpty(emailToValidate));
        }


    }
}

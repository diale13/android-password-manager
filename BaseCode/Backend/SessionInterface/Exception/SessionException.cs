using System;

namespace SessionInterface
{
    public class SessionException : Exception
    {
        public SessionException(string message) : base(message) { }
    }
}

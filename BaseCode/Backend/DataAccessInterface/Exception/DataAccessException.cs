using System;

namespace DataAccessInterface
{
    public class DataAccessException : Exception
    {
        public DataAccessException(string message) : base(message) { }
    }
}

using Domain;
using System;
using System.Collections.Generic;

namespace DataAccessInterface
{
    public interface IEmailHolderQuery
    {
        List<EmailHolder> GetHoldersByUser(Guid id);
    }
}

using Domain;
using System;
using System.Collections.Generic;

namespace BusinessLogicInterface
{
    public interface IAccountLogic
    {
        List<Account> GetAccountsByUser(Guid userId);
        void UpdateAccounts(Guid userId, List<Account> accountsToUpdate);
    }
}

using Domain;
using System;
using System.Collections.Generic;

namespace BusinessLogicInterface
{
    public interface IAppUserLogic
    {
        CreatedUserContainer CreateUser(AppUser user);
        void UpdateAndSubstituteAccountList(Guid userId, List<Account> newListOfAccounts);
    }
}

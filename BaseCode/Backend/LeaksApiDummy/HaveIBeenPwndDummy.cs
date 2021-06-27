using DataAccessInterface;
using Domain;
using ILeakApi;
using System;
using System.Collections.Generic;
using System.Linq;

namespace LeaksApiDummy
{
    public class HaveIBeenPwndDummy : ILeaksApi
    {
        private readonly IDataAccess<DummyLeak> dummyLeaksRepo;
        public HaveIBeenPwndDummy(IDataAccess<DummyLeak> _dummyLeaksRepo)
        {
            this.dummyLeaksRepo = _dummyLeaksRepo;
        }

        public List<Account> CheckAndAddNewLeaks(List<Account> accounts)
        {
            var accountsToUpdate = accounts;
            foreach (var (account, leak, newLeakedSiteFound) in from account in accountsToUpdate
                                                                let leak =
                                                                dummyLeaksRepo.GetByCondition(leak => leak.Email == account.EmailHolder.Email)
                                                                where leak != null
                                                                let newLeakedSiteFound = new LeakedSite(leak)
                                                                select (account, leak, newLeakedSiteFound))
            {
                account.EmailHolder.LeakedSites.Add(newLeakedSiteFound);
                account.EmailHolder.WasLeaked = true;
                dummyLeaksRepo.Remove(leak);
            }
            return accountsToUpdate;
        }

        public void DummyPostLeak(DummyLeak leakToAdd)
        {
            //Pray its valid
            dummyLeaksRepo.Add(leakToAdd);
        }
    }
}

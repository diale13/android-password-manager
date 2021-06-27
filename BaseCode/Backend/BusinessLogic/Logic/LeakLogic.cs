using BusinessLogicInterface;
using BusinessLogicInterface.Interfaces;
using DataAccessInterface;
using Domain;
using ILeakApi;
using System;
using System.Collections.Generic;
using System.Linq;

namespace BusinessLogic
{
    public class LeakLogic : ILeakLogic
    {
        private readonly ILeaksApi leaksApi;
        private readonly IAccountLogic accountLogic;
        private readonly INotificationLogic notificationLogic;

        public LeakLogic(ILeaksApi _leaksApi, IDataAccess<AppUser> repo, INotificationLogic _notificationLogic)
        {
            leaksApi = _leaksApi;
            accountLogic = new AccountLogic(repo);
            notificationLogic = _notificationLogic;
        }
      

        private List<Account> UpdateUserLeaks(Guid userId)
        {
            var userAccounts = accountLogic.GetAccountsByUser(userId);
            var accountsWithUpdatedLeaks = leaksApi.CheckAndAddNewLeaks(userAccounts);
            accountLogic.UpdateAccounts(userId, accountsWithUpdatedLeaks);
            return accountsWithUpdatedLeaks;
        }


        public List<LeakContainer> GetAllLeaks(Guid userId)
        {
            UpdateUserLeaks(userId);
            var allAccounts = accountLogic.GetAccountsByUser(userId);
            SetNotificationSent(userId, allAccounts);
            return CreateAllLeaksContainers(allAccounts).Distinct().ToList(); 
        }

        private List<LeakContainer> CreateAllLeaksContainers(List<Account> allAccounts)
        {
            var allLeaks = new List<LeakContainer>();
            foreach (var account in from account in allAccounts
                                    where account.HasLeaks()
                                    select account)
            {
                allLeaks.AddRange(from leak in account.EmailHolder.LeakedSites
                                  let leakContainerToAdd = new LeakContainer()
                                  {
                                      LeakedSiteName = account.Site,
                                      AsociatedEmail = account.EmailHolder.Email,
                                      Url = leak.Url
                                  }
                                  select leakContainerToAdd);
            }
            return allLeaks;
        }

        public List<LeakContainer> GetNewLeaks(Guid userId)
        {
            var newLeakedAccounts = UpdateUserLeaks(userId);
            var newLeaks = CreateNewLeaksContainer(newLeakedAccounts);
            SetNotificationSent(userId, newLeakedAccounts);
            notificationLogic.SendNotificationAsync(userId);
            return newLeaks.Distinct().ToList();
        }

        private List<LeakContainer> CreateNewLeaksContainer(List<Account> accounts)
        {
            var leaksInList = new List<LeakContainer>();
            foreach (var account in accounts)
            {
                var leakedSites = account.EmailHolder.LeakedSites;
                if (account.HasLeaks())
                {

                    leaksInList.AddRange(from leakedSite in leakedSites
                                         where !leakedSite.NotificationSent
                                         let leakToAdd = new LeakContainer()
                                         {
                                             AsociatedEmail = account.EmailHolder.Email,
                                             LeakedSiteName = leakedSite.Site,
                                             Url = leakedSite.Url
                                         }
                                         select leakToAdd
                                         );
                }
            }
            return leaksInList;
        }


        private void SetNotificationSent(Guid id, List<Account> accounts)
        {
            var listToUpdate = accounts;
            foreach (var account in listToUpdate)
            {
                foreach (var leakedSite in account.EmailHolder.LeakedSites)
                {
                    leakedSite.NotificationSent = true;
                }
            }
            accountLogic.UpdateAccounts(id, listToUpdate);
        }

        public void CreateDummy(DummyLeak l)
        {
            leaksApi.DummyPostLeak(l);
        }
    }
}

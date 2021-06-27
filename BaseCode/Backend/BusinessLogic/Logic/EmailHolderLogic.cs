using BusinessLogicInterface;
using DataAccessInterface;
using Domain;
using System;
using System.Collections.Generic;
using System.Linq;

namespace BusinessLogic
{
    internal class EmailHolderLogic
    {
        private readonly IEmailHolderQuery emailRepo;
        public EmailHolderLogic(IEmailHolderQuery _emailRepo)
        {
            this.emailRepo = _emailRepo;
        }

        public List<EmailHolder> GetEmailHoldersFromUser(Guid id)
        {
            try
            {
                return emailRepo.GetHoldersByUser(id);
            }
            catch (DataAccessException e)
            {

                throw new BusinessLogicException(e.Message);
            }
        }

        public List<Account> AdjustEmailHolders(AppUser user, List<Account> newListOfAccounts)
        {
            var userEmailHolders = GetEmailHoldersFromUser(user.Id);
            foreach (var (holder, account) in from holder in userEmailHolders
                                              from account in newListOfAccounts
                                              where account.EmailHolder.Email
                                              .Equals(holder.Email, StringComparison.OrdinalIgnoreCase)
                                              select (holder, account))
            {
                account.EmailHolder = holder;
            }
            return newListOfAccounts;
        }

        public AppUser AdjustHoldersForInsert(AppUser userToCreate)
        {
            var tempList = userToCreate.Accounts;
            var adjustedList = new List<Account>();
            foreach (var (account, emailHolder1) in from account in tempList.ToList()
                                                    let emailHolder1 = account.EmailHolder
                                                    select (account, emailHolder1))
            {
                adjustedList.Add(account);
                tempList.Remove(account);
                foreach (var account2 in from account2 in tempList.ToList()
                                         where emailHolder1.Equals(account2.EmailHolder)
                                         select account2)
                {
                    account2.EmailHolder = emailHolder1;
                    adjustedList.Add(account2);
                    tempList.Remove(account2);
                }
            }

            userToCreate.Accounts = adjustedList;
            return userToCreate;             
        }
    }
}

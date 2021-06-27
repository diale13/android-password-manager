using BusinessLogicInterface;
using DataAccessInterface;
using Domain;
using System;
using System.Collections.Generic;
using System.Linq;

namespace BusinessLogic
{
    public class AppUserLogic : IAppUserLogic
    {
        private readonly IDataAccess<AppUser> userRepo;
        private readonly EmailHolderLogic emailLogic;

        public AppUserLogic(IDataAccess<AppUser> repo, IEmailHolderQuery _emailRepo)
        {
            this.userRepo = repo;
            emailLogic = new EmailHolderLogic(_emailRepo);
        }

        public CreatedUserContainer CreateUser(AppUser userToCreate)
        {
            try
            {
                userToCreate.Validate();
                ValidateUniqueMail(userToCreate.MainEmail);
                userToCreate.Password = CryptoHelper.HashPassword(userToCreate.Password);
                userToCreate = emailLogic.AdjustHoldersForInsert(userToCreate);
                userRepo.Add(userToCreate);
                return CreatedUserContainer(userToCreate);
            }
            catch (DomainException e)
            {
                throw new BusinessLogicException(e.Message);
            }
            catch (DataAccessException e)
            {
                throw new BusinessLogicException(e.Message);
            }
        }

        public void UpdateAndSubstituteAccountList(Guid userId, List<Account> newListOfAccounts)
        {
            try
            {
                var user = userRepo.Get(userId);
                user.Accounts = emailLogic.AdjustEmailHolders(user, newListOfAccounts);
                user.Validate();
                userRepo.Update(user);
            }
            catch (DomainException e)
            {
                throw new BusinessLogicException(e.Message);
            }
            catch (DataAccessException e)
            {
                throw new BusinessLogicException(e.Message);
            }
        }

        private CreatedUserContainer CreatedUserContainer(AppUser userToCreate)
        {
            return new CreatedUserContainer()
            {
                DeviceId = userToCreate.DeviceId,
                MainEmail = userToCreate.MainEmail
            };
        }

        private void ValidateUniqueMail(string email)
        {
            var userWithMail = userRepo.GetByCondition(a => a.MainEmail == email);
            if (userWithMail != null)
            {
                throw new BusinessLogicException("Error: User with same email already exists");
            }
        }

    }
}

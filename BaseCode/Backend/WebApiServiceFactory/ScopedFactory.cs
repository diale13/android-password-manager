using BusinessLogic;
using BusinessLogicInterface;
using BusinessLogicInterface.Interfaces;
using DataAccess;
using DataAccessInterface;
using Domain;
using ILeakApi;
using LeaksApiDummy;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using SessionInterface;
using SessionLogic;

namespace WebApiServiceFactory
{
    public static class ScopedFactory
    {
        public static IServiceCollection ScopeSession(this IServiceCollection service)
        {
            service.AddScoped<ISessionManager, SessionManager>();
            return service;
        }

        public static IServiceCollection ScopeNotification(this IServiceCollection service)
        {
            service.AddScoped<INotificationLogic, NotificationLogic>();
            return service;
        }

        public static IServiceCollection ScopeAccount(this IServiceCollection service)
        {
            service.AddScoped<IAccountLogic, AccountLogic>();
            return service;
        }

        public static IServiceCollection ScopeAppUser(this IServiceCollection service)
        {
            service.AddScoped<IDataAccess<AppUser>, AppUserRepo>();
            service.AddScoped<IAppUserLogic, AppUserLogic>();
            return service;
        }

        public static IServiceCollection ScopeFileLogic(this IServiceCollection service)
        {
            service.AddScoped<IFileConverterLogic, FileLogic>();
            return service;
        }
        public static IServiceCollection ScopeLeaks(this IServiceCollection service)
        {
            service.AddScoped<ILeaksApi, HaveIBeenPwndDummy>();
            service.AddScoped<IDataAccess<DummyLeak>, DummyLeaksRepo>();
            service.AddScoped<ILeakLogic, LeakLogic>();
            return service;
        }

        public static IServiceCollection ScopeQueryUtility(this IServiceCollection service)
        {
            service.AddScoped<IEmailHolderQuery, EmailHolderQuery>();
            return service;
        }
        public static IServiceCollection ScopeContext(this IServiceCollection service)
        {
            var conectionString = ContextFactory.GetConnectionString();
            service.AddDbContext<DbContext, Context>(
              o => o.UseSqlServer(conectionString
              )
          );
            return service;
        }

    }
}

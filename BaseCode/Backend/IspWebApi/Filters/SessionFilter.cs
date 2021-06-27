using IspWebApi.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Filters;
using SessionInterface;
using System;

namespace IspWebApi.Filters
{
    public class SessionFilter : Attribute, IActionFilter
    {
        public void OnActionExecuting(ActionExecutingContext context)
        {
            string token = context.HttpContext.Request.Headers["Authorization"];
            if (token == null)
            {
                context.Result = new ContentResult()
                {
                    StatusCode = 401,
                    Content = MessagesOut.REQUIRED_TOKEN,
                };
                return;
            }
            var sessions = GetSessions(context);
            if (!sessions.IsValidToken(token))
            {
                context.Result = new ContentResult()
                {
                    StatusCode = 403,
                    Content = MessagesOut.INVALID_TOKEN,
                };
                return;
            }
        }

        private static ISessionManager GetSessions(ActionExecutingContext context)
        {
            return (ISessionManager)context.HttpContext.RequestServices.GetService(typeof(ISessionManager));
        }

        public void OnActionExecuted(ActionExecutedContext context)
        {

        }
    }
}

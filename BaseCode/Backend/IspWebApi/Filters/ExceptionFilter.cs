using System;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Filters;

namespace WebApi.Filters
{
    public class ExceptionFilter : IExceptionFilter
    {
        public void OnException(ExceptionContext context)
        {
            try
            {
                throw context.Exception;
            }
            catch (Exception e)
            {
                context.Result = new ContentResult()
                {
                    StatusCode = 500,
                    Content = $"A team of highly trained monkeys has been dispatched to solve this issue: {e.Message}"
                };
            }
        }
    }
}
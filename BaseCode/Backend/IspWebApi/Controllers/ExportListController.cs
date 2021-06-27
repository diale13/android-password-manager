using System;
using BusinessLogicInterface;
using BusinessLogicInterface.Interfaces;
using IspWebApi.Filters;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using SessionInterface;

namespace IspWebApi.Controllers
{

    [Route("api/exportedLists")]
    public class ExportListController : Controller
    {
        private readonly IFileConverterLogic fileLogic;
        private readonly ISessionManager sessionLogic;


        public ExportListController(IFileConverterLogic _logic, ISessionManager _session)
        {
            this.fileLogic = _logic;
            sessionLogic = _session;
        }



        /// <remarks>
        /// Obtains users accounts and zips them in a base64 file with a password protection (the password is the user's email)
        /// </remarks>        
        /// <return> New leaks for user</return>
        /// <response code="200">Ok: returns base64 string</response>
        /// <response code="401">No token provided</response>
        /// <response code="403">Invalid token</response>
        /// <response code="500">Internal server error: the server or database may be having problems</response>
        [SessionFilter]
        [HttpGet("zip")]
        public IActionResult GetFileAsZip()
        {
            try
            {
                var id = GetUserToken();
                var fileAsEncodedString = fileLogic.GetExportedListAsZip(id);
                return Ok(fileAsEncodedString);
            }
            catch (BusinessLogicException e)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, e.Message);
            }
        }

        private Guid GetUserToken()
        {
            string token = Request.Headers["Authorization"];
            return sessionLogic.GetUser(token);
        }
    }
}
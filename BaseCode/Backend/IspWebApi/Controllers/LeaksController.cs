using System;
using BusinessLogicInterface;
using IspWebApi.Filters;
using IspWebApi.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using SessionInterface;

namespace IspWebApi.Controllers
{
    [Route("api/leaks")]
    public class LeaksController : Controller
    {
        private readonly ILeakLogic leakLogic;
        private readonly ISessionManager sessionlogic;


        public LeaksController(ILeakLogic logic, ISessionManager _sessionLogic)
        {
            this.leakLogic = logic;
            this.sessionlogic = _sessionLogic;
        }


        /// <remarks>
        /// Obtains the new leaks (not sent before) found in databreaches for a given user, determined by the user auth token
        /// </remarks>        
        /// <return> New leaks for user</return>
        /// <response code="200">Ok: returns new leaks list</response>
        /// <response code="401">No token provided</response>
        /// <response code="403">Invalid token</response>
        /// <response code="500">Internal server error: the server or database may be having problems</response>
        [SessionFilter]
        [HttpGet("new")]
        public IActionResult GetNewLeaks()
        {
            try
            {
                var userId = GetUserToken();
                return Ok(leakLogic.GetNewLeaks(userId));
            }
            catch (BusinessLogicException e)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, e.Message);
            }
        }


        /// <remarks>
        /// Obtains the all leaks (even those that where sent before) found in databreaches for a given user, determined by the user auth token
        /// </remarks>        
        /// <return> All leaks for user</return>
        /// <response code="200">Ok: returns all leaks list</response>
        /// <response code="401">No token provided</response>
        /// <response code="403">Invalid token</response>
        /// <response code="500">Internal server error: the server or database may be having problems</response>
        [SessionFilter]
        [HttpGet("all")]
        public IActionResult GetAllLeaks()
        {
            try
            {
                var userId = GetUserToken();
                return Ok(leakLogic.GetAllLeaks(userId));
            }
            catch (BusinessLogicException e)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, e.Message);
            }
        }

        private Guid GetUserToken()
        {
            string token = Request.Headers["Authorization"];
            return this.sessionlogic.GetUser(token);
        }

        /// <remarks>
        /// Allows the user to post a dummy leak for a given user, in final build this should be deleted and use a real leaks API like HaveIbeenPwnd
        /// </remarks>        
        /// <return> All leaks for user</return>
        /// <response code="201">Created</response>
        /// <response code="401">No token provided</response>
        /// <response code="403">Invalid token</response>
        /// <response code="500">Internal server error: the server or database may be having problems</response>
        [HttpPost("dummy")]
        public IActionResult Post([FromBody] DummyModelIn dummyLeakModel)
        {
            try
            {
                if (!dummyLeakModel.Validate()) return BadRequest("Invalid dummy");
                leakLogic.CreateDummy(dummyLeakModel.ToEntity());
                return Ok("Dummy created, please delete in final app version");
            }
            catch (BusinessLogicException e)
            {
                return BadRequest(e.Message);
            }
        }


    }
}
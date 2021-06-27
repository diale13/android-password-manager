using BusinessLogicInterface;
using IspWebApi.Filters;
using IspWebApi.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using SessionInterface;
using System;
using System.Linq;

namespace IspWebApi.Controllers
{
    [Route("api/users")]
    public class UsersController : Controller
    {

        private readonly IAppUserLogic userLogic;
        private readonly ISessionManager sessionLogic;

        public UsersController(IAppUserLogic _userLogic, ISessionManager _sessionLogic)
        {
            this.userLogic = _userLogic;
            this.sessionLogic = _sessionLogic;
        }

        /// <remarks>
        /// Creates an user 
        /// </remarks>        
        /// <return> User information </return>
        /// <response code="201">Created: user information</response>
        /// <response code="400">Bad request: validation errors</response>
        [HttpPost]
        public IActionResult Post([FromBody] UserModelIn userModel)
        {
            try
            {
                var userToCreate = userModel.ToEntity();
                var created = userLogic.CreateUser(userToCreate);
                return StatusCode(StatusCodes.Status201Created, created);
            }
            catch (BusinessLogicException e)
            {
                return BadRequest(e.Message);
            }
        }

        /// <remarks>
        /// Updates an user accounts. Needs to be the new list of all the accounts the user has. 
        /// </remarks>        
        /// <return> ok message </return>
        /// <response code="200">Ok: user accounts updated</response>
        /// <response code="400">Bad request: validation errors in account model</response>
        /// <response code="401">No token provided</response>
        /// <response code="403">Invalid token</response>
        [SessionFilter]
        [HttpPut("accounts")]
        public IActionResult Put([FromBody] AccountUpdateModel accountUpdateModel)
        {
            try
            {
                var id = GetUserToken();
                var accountList = (from accountModel in accountUpdateModel.AccountModels
                                   select accountModel.ToEntity(id)).ToList();
                Guid userToken = GetUserToken();
                userLogic.UpdateAndSubstituteAccountList(userToken, accountList);
                return Ok("Updated!");
            }
            catch (BusinessLogicException e)
            {
                return BadRequest(e.Message);
            }
        }


        private Guid GetUserToken()
        {
            string token = Request.Headers["Authorization"];
            return sessionLogic.GetUser(token);
        }
    }
}
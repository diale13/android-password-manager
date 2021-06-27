using IspWebApi.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using SessionInterface;

namespace IspWebApi.Controllers
{
    [Route("api/sessions")]
    public class SessionController : Controller
    {
        private readonly ISessionManager sessionLogic;
        public SessionController(ISessionManager logic)
        {
            sessionLogic = logic;
        }

        /// <remarks>
        /// Performs login with username and password
        /// </remarks>        
        /// <return> Authorization token </return>
        /// <response code="200">Ok: returns auth token</response>
        /// <response code="400">Bad request: invalid username or password</response>
        /// <response code="500">Internal server error: the server or database may be having problems</response>
        [HttpPost]
        public IActionResult Post([FromBody] LogInModel model)
        {
            try
            {
                if (!model.IsValid())
                {
                    return BadRequest("Bad username or password");
                }
                var token = sessionLogic.CreateToken(model.Email, model.Password);
                if (token == null)
                {
                    return Ok("Incorrect username or password");
                }
                return Ok(token);
            }
            catch (SessionException e)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, e.Message);
            }
        }
    }
}
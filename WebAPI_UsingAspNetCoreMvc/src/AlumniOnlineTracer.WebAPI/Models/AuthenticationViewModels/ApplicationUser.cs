using OpenIddict;

namespace AlumniOnlineTracer.WebAPI.Models.AuthenticationViewModels
{
	// Add profile data for application users by adding properties to the ApplicationUser class
	public class ApplicationUser : OpenIddictUser
	{
		public string GivenName { get; set; }
	}
}

using AlumniOnlineTracer.WebAPI.Models.AuthenticationViewModels;
using Microsoft.EntityFrameworkCore;
using OpenIddict;

namespace AlumniOnlineTracer.WebAPI.Data
{
	public class AuthenticationDbContext : OpenIddictDbContext<ApplicationUser, ApplicationRole>
	{
		public AuthenticationDbContext(DbContextOptions options) : base(options)
		{ }

		protected override void OnModelCreating(ModelBuilder builder)
		{
			base.OnModelCreating(builder);
		}
	}
}
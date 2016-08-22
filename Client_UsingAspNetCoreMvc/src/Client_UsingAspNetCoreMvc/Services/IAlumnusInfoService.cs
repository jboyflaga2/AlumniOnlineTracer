using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace AlumniOnlineTracer.Client.Services
{
	public interface IAlumnusInfoService
	{
		Task<string> GetAllAlumnusInfo();
		Task<string> GetAlumnusInfo(Guid id);
	}
}

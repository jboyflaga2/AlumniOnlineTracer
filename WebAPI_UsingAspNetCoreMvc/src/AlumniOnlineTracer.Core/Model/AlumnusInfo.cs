using System;
using System.Collections.Generic;
using System.Linq;

namespace AlumniOnlineTracer.Core.Model
{
    public class AlumnusInfo
    {
		public Guid Id { get; protected set; }
		public string FirstName { get; set; }
		public string LastName { get; set; }

		public AlumnusInfo()
		{
			Id = Guid.NewGuid();
		}

		public ICollection<EmploymentRecord> EmploymentRecords { get; set; }

		public EmploymentRecord FindEmploymentRecord(Guid id)
		{
			return EmploymentRecords.SingleOrDefault(r => r.Id == id);
		}

		public void AddEmploymentRecord(EmploymentRecord employmentRecord)
		{
			EmploymentRecords.Add(employmentRecord);
		}


	}
}

package Filter;

import java.util.ArrayList;
import java.util.Date;

import com.example.demo.Mail.Mail;

public class CriteriaDate implements Criteria {

	@Override
	public ArrayList<Mail> meetCriteria(ArrayList<Mail> mails,String filter) {
		
		ArrayList<Mail> Res=new ArrayList<Mail>();
		for(Mail mail: mails) {
			Date currentDate = new Date(Long.parseLong(mail.getMailHeader().getDate()));
			if(currentDate.toString().equals(filter)) {
				Res.add(mail);
			}
		}
		return Res;
		
		
	}
}

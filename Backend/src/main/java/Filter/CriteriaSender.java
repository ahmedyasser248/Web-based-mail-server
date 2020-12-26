package Filter;

import java.util.ArrayList;


import com.example.demo.Mail.Mail;

public class CriteriaSender implements Criteria {

	@Override
	public ArrayList<Mail> meetCriteria(ArrayList<Mail> mails,String filter) {
		
		ArrayList<Mail> Res=new ArrayList<Mail>();
		for(Mail mail: mails) {
			if(mail.getMailHeader().getSender().equals(filter)) {
				Res.add(mail);
			}
		}
		return Res;
		
		
	}

}

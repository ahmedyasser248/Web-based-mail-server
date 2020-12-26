package Filter;

import java.util.ArrayList;

import com.example.demo.Mail.Mail;

public class CriteriaReceivers implements Criteria {
	@Override
	public ArrayList<Mail> meetCriteria(ArrayList<Mail> mails,String filter) {
		
		ArrayList<Mail> Res=new ArrayList<Mail>();
		for(Mail mail: mails) {
		  ArrayList<String> Rec=mail.getMailHeader().getReceivers();
			for(String reciver : Rec)
			if(reciver.equals(filter)) {
				Res.add(mail);
			}
		}
		return Res;
		
		
	}
}

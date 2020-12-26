package Filter;

import java.util.ArrayList;

import com.example.demo.Mail.Mail;

public interface Criteria {
	public ArrayList<Mail> meetCriteria(ArrayList<Mail> mails,String filter);
}

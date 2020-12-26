
export  class User{
    constructor(Email,password,firstName,lastName){
        this.Email=Email
        this.password=password
        this.firstName=firstName
        this.lastName=lastName
    }
}

export  class MessageBody{
    constructor(Body){
        this.Body=Body
    }
}

export class MailHeader{
    constructor(Sender,Receivers,Subject,date,Priority){
        this.Sender=Sender
        this.Receivers=Receivers
        this.Subject=Subject
        this.date=date
        this.Priority=Priority
    }
}

export class MailAttachements{
    constructor(attachements){
        this.attachements = attachements
    }
}
export class MailID{
    constructor(userID,sourceID,dateAsID){
        this.userID = userID
        this.sourceID = sourceID
        this.dateAsID = dateAsID
    }
}


export class Mail{
    constructor(mailAttachements,mailHeader,messageBody,mailID){
        this.mailAttachements=mailAttachements
        this.mailHeader=mailHeader
        this.messageBody=messageBody
        this.mailID=mailID
    }
}

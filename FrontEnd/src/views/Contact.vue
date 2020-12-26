<template>
  <div class="contact">
    <Navbar :username="username" navigation='Contact'/>
   
     <v-app-bar class="mb-10" :collapse="!collapseOnScroll" absolute dark>
          <v-icon class="pr-6">mdi-magnify</v-icon>

          <v-text-field
            placeholder="Search available on Contact's name"
            v-model="element"
            hide-details
            single-line
          ></v-text-field>
          
              <v-btn
                fab
                text
                @click="ActualSearch"
                v-if="element.length"
              >
                <v-icon color="success">done</v-icon>
              </v-btn>
            
          
              <v-btn
                fab
                text
                @click="undone"
                v-if="collapseOnScroll"
              >
                <v-icon color="error">close</v-icon>
              </v-btn>
            
          

          <v-spacer></v-spacer>

          <v-checkbox
            v-model="collapseOnScroll"
            color="white"
            hide-details
          ></v-checkbox>
        </v-app-bar>
   <br>
   <br>
   
   
    <!--keep thing sin centre-->
    <v-container class="my-5">

      <!--add contacts-->
      <v-dialog v-model="dialog" width="600">
        <template v-slot:activator="{ on, attrs }">
          <v-btn color="success" v-bind="attrs" v-on="on" :disabled="!sortFlag">
            Add Contact
            <v-icon right dark>
              mdi-plus
            </v-icon>
          </v-btn>
        </template>
        <!--viewing contesnt of adding contact-->
        <v-card>
          <v-card-title class="grey lighten-2">
            Add Contact
            <v-spacer></v-spacer>
          <v-btn
            color="error"
            text
            fab
            
            @click="dialog= false"
          >
            <v-icon>close</v-icon>
          </v-btn>
          </v-card-title>
          
          <v-card-text>
            <v-form class="px-3" ref="fo">
              <v-text-field
                label="Contact"
                v-model="Contact"
                prepend-icon="person"
                autocomplete="off"
                :rules="inputRules"
              ></v-text-field>

              <v-text-field
                label="Email"
                v-model="emails"
                prepend-icon="edit"
                autocomplete="off"
                hint="you should put only one email and if you want to add more emails edit from contact"
              ></v-text-field>

              <v-row>
                <v-btn text class="success mx-0" @click="addcontact">Add</v-btn>
              </v-row>
            </v-form>
          </v-card-text>
        </v-card>
      </v-dialog>

      <br>
      <br>
        <v-row>
     <v-menu offset-y>
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                text
                color="black"
                v-bind="attrs"
                v-on="on"
                :disabled="!sortFlag"
              >
                <span>Sort</span>
                <v-icon right>expand_more</v-icon>
              </v-btn>
            </template>
          <v-list dark>
          <v-list-item
            @click="sortBy(false)"
          >
            <v-list-item-title>Ascending</v-list-item-title>
          </v-list-item>
           <v-list-item
            
            
            @click="sortBy(true)"
          >
            <v-list-item-title>Descending</v-list-item-title>
          </v-list-item>
        
         
          </v-list>
        </v-menu>
    <v-spacer></v-spacer>
      <v-tooltip top>
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              color="white"
              fab
              x-small
              text
              @click="refresh"
              v-bind="attrs"
              v-on="on"
            >
              <v-icon> refresh </v-icon>
            </v-btn>
          </template>
          <span>Refresh</span>
        </v-tooltip>
     <v-tooltip top>
         <template v-slot:activator="{ on, attrs }">  
              <v-btn color="white" fab x-small text @click="dec" v-bind="attrs" v-on="on">
                <v-icon>
                  keyboard_arrow_left
                  </v-icon>   
              </v-btn>
         </template>
          <span>Previous Page</span>
        </v-tooltip>

    
     <h4 style="color : white" class="pa-1">{{this.pages}}</h4>
      <v-tooltip top>
         <template v-slot:activator="{ on, attrs }">  
              <v-btn color="white" fab x-small text @click="inc" v-bind="attrs" v-on="on">
               <v-icon>
                  keyboard_arrow_right
                  </v-icon> 
              </v-btn>
         </template>
          <span>Next Page</span>
        </v-tooltip>
    </v-row>
     
    

    <v-toolbar
      v-if="select.length"
      dark
    > 
      <v-btn
        icon
        @click="select = []"
      >
        <v-icon>mdi-close</v-icon>
      </v-btn>

      <v-toolbar-title>
        {{select.length}} Selected
      </v-toolbar-title>

      <v-spacer></v-spacer>

      <v-scale-transition>
        <v-btn
          v-if="select.length"
          key="delete"
          icon
          @click="deleteContacts"
        >
          <v-icon>mdi-delete</v-icon>
        </v-btn>
      </v-scale-transition>
    </v-toolbar>

      <!--viewing cntacts -->
      <v-card>
      <v-row no-gutters>
        <v-col
        >
        <v-card v-for="(contact, index) in contacts"
          :key="contact.contact" flat>
          <v-dialog v-model="removeTagDialogs[index]" width="800">
            <template v-slot:activator="{ on, attrs }">
              <v-card flat
                class=" text-center pt-2 "
                
                v-bind="attrs"
                v-on="on"
              >
              <v-row>
              <v-col cols="2">
                <v-responsive>
                  <v-avatar size="75" class="grey lighten-2">
                    <v-icon>person</v-icon>
                  </v-avatar>
                </v-responsive>
                </v-col>
              <v-col cols="5">
                <v-card-text>
                  <h3 class="h4 mt-3">{{ contact.contact }}</h3>
                </v-card-text>
                </v-col>
             
              </v-row>
              </v-card>
            </template>
            <!--viewing content of contact-->
            <v-card>
              <v-card-title class=" grey lighten-2">
                <div class="headline ">{{ contact.contact }}</div>
                <v-spacer></v-spacer>
                <v-dialog v-model="edits[index]" width="600">
                  <!--edit button-->
                  <template v-slot:activator="{ on, attrs }">
                    <v-btn text fab v-bind="attrs" v-on="on">
                      <v-icon>edit</v-icon>
                    </v-btn>
                  </template>
                  <!--viewing editting form-->
                  <v-card>
                    <v-card-title class="headline grey lighten-2">
                      Edit Contact
                      <v-spacer></v-spacer>
                      <v-btn
                        color="error"
                        text
                        fab
                        @click="edits[index] = false"
                      >
                        <v-icon>close</v-icon>
                      </v-btn>
                    </v-card-title>
                    <v-form class="px-3">
                      <v-row>
                        <v-col cols="9">
                          <v-text-field
                            label="new contact Name"
                            v-model="newContacts[index]"
                            prepend-icon="edit"
                            autocomplete="off"
                          ></v-text-field>
                        </v-col>
                        <v-col cols="1">
                          <v-btn color="success pl-2" text @click="editContact(contact.contact,newContacts[index],index)">
                            <span>Edit</span>
                            <v-icon right>edit</v-icon>
                          </v-btn>
                        </v-col>
                      </v-row>
                    </v-form>
                  </v-card>
                </v-dialog>
              </v-card-title>
              <!--content of contact-->

              <v-chip-group active-class="primary--text">
                <v-chip
                  v-for="i in contact.Email_Adresses"
                  :key="i"
                  class="ma-2"
                  close
                  @click:close="deleteName(contact.contact, i)"
                >
                  {{ i }}
                </v-chip>
              </v-chip-group>

              <v-divider></v-divider>

              <v-card-actions>
                <v-dialog v-model="adds[index]" width="600">
                  <!--edit button-->
                  <template v-slot:activator="{ on, attrs }" >
                    <v-btn text  v-bind="attrs" v-on="on" color="success">
                      <span class="pl-5">Add New Email</span>
                      <v-icon right>mdi-plus</v-icon>
                    </v-btn>
                  </template>
                  <!--viewing adding form-->
                  <v-card>
                    <v-card-title class="headline grey lighten-2">
                      Add new Email
                      <v-spacer></v-spacer>
                      <v-btn
                        color="error"
                        text
                        fab
                        @click="adds[index] = false"
                      >
                        <v-icon>close</v-icon>
                      </v-btn>
                    </v-card-title>
                    <v-form class="px-3">
                      <v-row>
                        <v-col cols="10">
                          <v-text-field
                            label="add new email"
                            v-model="newEmails[index]"
                            prepend-icon="edit"
                            autocomplete="off"
                            hint="you should put only one email and if you want to add more emails edit from contact"
                          ></v-text-field>
                        </v-col>
                        <v-col cols="1">
                          <v-btn color="success pl-2" text  @click="addNewName(contact.contact,newEmails[index],index)">
                            <span>Add</span>
                            <v-icon right>mdi-plus</v-icon>
                          </v-btn>
                        </v-col>
                      </v-row>
                    </v-form>
                  </v-card>
                </v-dialog>
                <v-spacer></v-spacer>
                <v-btn
                  color="error"
                  text
                  @click="removeTagDialogs[index] = false"
                >
                  close
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
          </v-card>
        </v-col>

<!--starting messaging here-->
           <v-col cols="2" >
         <v-card flat  :key="contact.contact" v-for="(contact,index) in contacts" class="pb-7 pt-11 pl-6">
           <v-dialog  v-model="message[index]"  width="600">
      <template v-slot:activator="{ on, attrs }">
        <v-btn  color="grey" v-bind="attrs" v-on="on" @click="addData(contact.Email_Adresses)">
          Message
          
        </v-btn>
      </template>
      <v-card>
        <v-card-title class="grey lighten-2">
          Send an Email
          <v-spacer></v-spacer>
          <v-btn
                  color="error"
                  text
                  @click="close(index)"
                >
                  <v-icon>close</v-icon>
                </v-btn>
        </v-card-title>
          <v-spacer></v-spacer>
                
        <v-card-text>
          <v-form class="px-3" ref="form">
            <v-text-field
              label="To"
              v-model="To"
              prepend-icon="person"
              autocomplete="off"
              :rules="inputRules2"
              hint="if you want to send for more than one type emails with , between them "
            ></v-text-field>

            <v-text-field
              label="Subject"
              v-model="Subject"
              prepend-icon="edit"
              autocomplete="off"
              :rules="inputRules2"
            ></v-text-field>
            <v-textarea
              label="Message"
              v-model="content"
              prepend-icon="message"
              autocomplete="off"
              :rules="inputRules2"
            ></v-textarea>
           <v-row>
            <v-col>
              <v-menu offset-y>
                <template v-slot:activator="{ on, attrs }">
                  <v-btn text color="grey" v-bind="attrs" v-on="on">
                    <span>Priority</span>
                    <v-icon right>expand_more</v-icon>
                  </v-btn>
                </template>
                <v-list dark>
                  <v-list-item @click="priority = 'Superhigh'">
                    <v-list-item-title>Superhigh</v-list-item-title>
                  </v-list-item>
                  <v-list-item @click="priority = 'high'">
                    <v-list-item-title>high</v-list-item-title>
                  </v-list-item>
                  <v-list-item @click="priority = 'medium'">
                    <v-list-item-title>medium</v-list-item-title>
                  </v-list-item>
                  <v-list-item @click="priority = 'low'">
                    <v-list-item-title>low</v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
            </v-col>
            <v-col  >
              <h3 class="pa-3">
                 {{priority}}
              </h3>
            </v-col>
           </v-row>
           <v-row>
            <v-btn text class="success mx-0" @click="submit(index)" >Send</v-btn>
            <v-spacer></v-spacer>
            <v-file-input id="choose" multiple show-size counter placeholder="Choose File"
              truncate-length="15" ref="myFileInput"
            ></v-file-input >
            <v-spacer></v-spacer>
            <v-btn text class="error mx-0" @click="draft(index)" >Draft</v-btn>
            </v-row>
          </v-form>
        </v-card-text>
      </v-card>
    </v-dialog>
          </v-card>
      </v-col>



        <v-col cols="1" >
         <v-card flat  :key="contact.contact" v-for="contact in contacts" class="pb-5 pt-5 pl-6">
         <v-checkbox
            v-model="select"
            :value="contact.contact"
          ></v-checkbox>
          </v-card>
      </v-col>
      </v-row>
      </v-card>
    </v-container>
  </div>
</template>

<script>
import Navbar from "@/components/Navbar";
import axios from "axios";
import {
  Mail,
  MessageBody,
  MailHeader,
  MailAttachements,
  MailID
} from "@/components/classes.js";
export default {
  name: "Contact",
  components: { Navbar },
  data() {
    return {
      message: {},
      To: "",
      From: this.username,
      Subject: "",
      content: "",
      priority: "low",
      date: new Date().toISOString(),
      inputRules2: [
        (v) => !!v || "This field is required",
        (v) => v.length >= 1 || "Minimum length is 1 characters",
      ],


      searchfor: "",
      sortFlag:true,
      Insearch:false,
      element: "",
      collapseOnScroll: false,
      dialog : false,
      select :[],
      reverse: false,
      pages :1,
      removeTagDialogs: {},
      edits: {},
      adds: {},
      newContacts: {},
      newEmails: {},
      Contact: "",
      emails: "",
      contacts: null,
      username: this.$route.params.username,
      team: [],
      inputRules: [(v) => !!v || "This field is required"],
    };
  },
  async mounted() {
    //this.contacts = await this.loadContacts(this.pages);
    await this.sortBy(this.reverse)
  },
  methods: {
   async refresh(){
      this.message= {}
      this.To= ""
      this.From= this.username
      this.Subject= ""
      this.content= "",
      this.priority= "low",
      this.date= new Date().toISOString()
     

      this.searchfor= ""
      this.sortFlag=true
      this.Insearch=false
      this.element= ""
      this.collapseOnScroll= false
      this.dialog = false
      this.select =[],
      this.reverse= false,
      this.pages =1,
      this.removeTagDialogs= {}
      this.edits= {}
      this.adds= {}
      this.newContacts= {}
      this.newEmails= {}
      this.Contact= ""
      this.emails= ""
      this.contacts= null
      this.username= this.$route.params.username
      this.team= []
      await this.sortBy(this.reverse)
      location.reload()

    },
    close(p){
       this.message[p]=false
      this.clearPopup()
      this.$refs.myFileInput.reset();
    },
    addData(p){
      for(let i=0;i<p.length;i++){
          this.To += p[i]
          if(i!= (p.length -1)){
            this.To += ','
          }
      }
    },
    clearPopup(){
       this.To = ""
        this.Subject = ""
        this.content = ""
        this.priority = "low"
    },
      submit(p) {
      let array =this.To.split(',')
      if (this.$refs[`form`][0].validate()
) {
      var bodyFormData = new FormData();
      bodyFormData.append("mail",JSON.stringify(new Mail(
                new MailAttachements(null),
                new MailHeader(
                  this.username,
                  array,
                  this.Subject,
                  this.date,
                  this.priority
                ),
                new MessageBody(this.content),
                new MailID(null,null,null))
              ))
              /*console.log(this.From,
                  array,
                  this.Subject,
                  this.date,
                  this.priority)*/
      let g = document.getElementById("choose").files           
      for (let i=0;i<g.length;i++){
        bodyFormData.append("file",g[i])
      }
      var size=0
      for (let i=0;i<g.length;i++){
        size = size + g[i].size
      }
      if(size > 1001000000){
        alert("You exceeded the maximum size for the attachments which is 1giga ,Kindly Remove the attachments")
      }else{
      if(size > 20000000){
        alert("Uploading high size attachments will take a few moments >> Press OK to continue")
      }
        axios({
                  method: 'post',
                  url: 'http://localhost:8085/sendMail',
                  data: bodyFormData,
                  headers: {'Content-Type': 'multipart/form-data' }
                  })
          .then((response) => {
            if(response.data){
                this.clearPopup();
                this.message[p] = false;
                //this.$refs.myFileInput.reset();
                //location.reload()
                this.refresh()
            }
            else{
              alert('one or all recievers are wrong')
            }
          });
      }  
}
    },
    draft(p){
      let array =this.To.split(',')
      let g = document.getElementById("choose").files
      if(this.To=='' && this.Subject=='' && this.content=='' && g.length==0){
         this.message[p] = false;
         alert("all data are empty so nothing is saved")
         return
      }
        var bodyFormData = new FormData();
      bodyFormData.append("mail",JSON.stringify(new Mail(
                new MailAttachements(null),
                new MailHeader(
                  this.username,
                  array,
                  this.Subject,
                  this.date,
                  this.priority
                ),
                new MessageBody(this.content),
                new MailID(null,null,null))
              ))          
      for (let i=0;i<g.length;i++){
        bodyFormData.append("file",g[i])
      }
      var size=0
      for (let i=0;i<g.length;i++){
        size = size + g[i].size
      }
      if(size > 1001000000){
        alert("You exceeded the maximum size for the attachments which is 1giga ,Kindly Remove the attachments")
      }else{
      if(size > 20000000){
        alert("Uploading high size attachments will take a few moments >> Press OK to continue")
      }
      axios({
                  method: 'post',
                  url: 'http://localhost:8085/saveDraft',
                  data: bodyFormData,
                  headers: {'Content-Type': 'multipart/form-data' }
                  })

        this.clearPopup();
        this.message[p] = false;
        //this.$refs.myFileInput.reset();
        //location.reload()
        this.refresh()
      }
      } ,
    clear() {
      (this.Contact = ""), (this.emails = "");
    },
     async undone() {
      this.sortFlag=true
      this.Insearch=false
      this.select = [];
      this.element = "";
      this.contacts=null;
      this.pages=1
      this.reverse=false
      this.collapseOnScroll = false;
      await this.sortBy(this.reverse)
      //ocation.reload()
    },
    async ActualSearch(){ 
      this.Insearch=true
      this.sortFlag=false
      console.log(this.pages)
      let response = await axios.get("http://localhost:8085/SearchCon", {
        params: {
          user: this.$route.params.username,
          conToSearch: this.element,
          pageNum: this.pages,
        },
      });
      if (response.status === 200) {
        if(await response.data.length==0){
          alert("There is no such thing")
        }else{
          this.contacts = null
          this.contacts= await response.data
        }
      }
    },
    async sortBy(p){
      this.reverse = p
      let response = await axios.get("http://localhost:8085/SortCon", {
              params: {
                user: this.username,
                sortType: "name",
                pageNum: this.pages,
                reversed: this.reverse
              },
            });
             if (response.status === 200) {
               this.contacts=null
                this.contacts = await response.data
            }  
    },
     async dec(){
      if(this.pages>1){
        
        this.pages--
        this.contacts=null
        if(this.Insearch==true){
            await this.ActualSearch()
        }
        else{
          await this.sortBy(this.reverse)
        }
         
        }
        
    },
    async inc(){
      
       if(this.Insearch==true){
         this.pages++
          this.contacts=null
            await this.ActualSearch()
        }
        else{
          this.pages++
          this.contacts=null
          await this.sortBy(this.reverse)
        }
    },
    async deleteContacts(){
      for(let i=0;i<this.select.length;i++){
          await this.deleteContact(this.select[i])
      }
      this.select=[]
      this.contacts=null
      if(this.Insearch==false){
        
        await this.sortBy(this.reverse)
      }else{
       
        await this.ActualSearch()
      }
      //this.contacts = await this.loadContacts(this.pages);
     
      
    },
    async addNewName(p,name,index){
      let response = await this.addName(p,name)
      if(response){
        this.adds[index]=false
        //this.removeTagDialogs[index]=false
        this.newEmails={}
        this.contacts=null
        //this.contacts = await this.loadContacts(this.pages);
        await this.sortBy(this.reverse)
      }
      else{
        alert("one or more emails are wrong and not in system  , you added it before or you put an extra space");
      } 
    },
    async editContact(p,newContact,index){
      var reg3 = /^([a-zA-Z0-9_\\.-]+)$/;
          if(!reg3.test(p)){
            alert('Contact should contain letters and numbers and . or - or _  only')
          }
          else{
            await axios.get("http://localhost:8085/editContacts", {
              params: {
                user: this.username,
                pageNum: this.pages,
                contact: p,
                newContact: newContact
              },
            });
            this.edits[index]=false
            this.removeTagDialogs[index]=false
            this.newContacts={}
            this.contacts=null
            //this.contacts = await this.loadContacts(this.pages);
            await this.sortBy(this.reverse)
          }
    },
    async deleteContact(p) {
      await axios.get("http://localhost:8085/delContacts", {
        params: {
          user: this.username,
          contact: p,
        },
      });
    },
    async add(p) {
        var reg3 = /^([a-zA-Z0-9_\\.-]+)$/;
           if(!reg3.test(p)){
            alert('Contact should be more than 8 Characters and contain letters and numbers and . or - or _  only')
            return false
          }
          else{
            let res = await axios.get("http://localhost:8085/addContacts", {
              params: {
                user: this.username,
                contact: p,
              },
            });
            return res.data;
          }
    },
    async deleteName(p, name) {
      let res = await axios.get("http://localhost:8085/delNames", {
        params: {
          user: this.username,
          contact: p,
          name: name,
        },
      });
      this.contacts=null
      //this.contacts = await this.loadContacts(this.pages);
      await this.sortBy(this.reverse)
      return res.data;
    },
    async addName(p, name) {
      let resopnse = await axios.get("http://localhost:8085/addNames", {
        params: {
          user: this.username,
          contact: p,
          name: name,
        },
      });
      return resopnse.data;
    },
    async loadContacts(num) {
      let resopnse = await axios.get("http://localhost:8085/loadContacts", {
        params: {
          user: this.username,
          pageNum: num,
        },
      });
      return resopnse.data;
    },
    async addcontact() {
      if (this.$refs.fo.validate()) {
        let res = await this.add(this.Contact);
        if(!res){
          return
        }
        if(this.emails != ''){
          let response = await this.addName(this.Contact, this.emails);
          console.log(response);
          if (response) {
            this.clear();
            this.dialog = false;
            this.contacts = null;
            //this.contacts = await this.loadContacts(this.pages);
            await this.sortBy(this.reverse)
          } else {
            await this.deleteContact(res);
            alert("one or more emails are wrong and not in system , you added it before or you put an extra space");
          }
        }
        else{
          this.clear();
            this.dialog = false;
            this.contacts = null;
            //this.contacts = await this.loadContacts(this.pages);
            await this.sortBy(this.reverse)
        }
      }
    },
  },
};
</script>
<style >
</style>
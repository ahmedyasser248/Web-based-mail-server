<template>
  <div class="draft">
    <Navbar :username="username" navigation="Draft" />
    <!--keep thing sin centre-->
    <v-container class="my-5 mt-15">
      <v-row class="mb-3">
        <v-menu offset-y>
          <template v-slot:activator="{ on, attrs }">
            <v-btn text color="grey" v-bind="attrs" v-on="on" :disabled="!sortFlag">
              <span>Sort By</span>
              <v-icon right>expand_more</v-icon>
            </v-btn>
          </template>
          <v-list dark>
            <v-list-item @click="sortBy('Subject')">
              <v-list-item-title>Subject</v-list-item-title>
            </v-list-item>
            <v-list-item @click="sortBy('Sender')">
              <v-list-item-title>Sender</v-list-item-title>
            </v-list-item>
            <v-list-item @click="sortBy('Date')">
              <v-list-item-title>Date</v-list-item-title>
            </v-list-item>
            <v-list-item @click="sortBy('Priority')">
              <v-list-item-title>Priority</v-list-item-title>
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
            <v-btn
              color="white"
              fab
              x-small
              text
              @click="dec"
              v-bind="attrs"
              v-on="on"
            >
              <v-icon> keyboard_arrow_left </v-icon>
            </v-btn>
          </template>
          <span>Previous Page</span>
        </v-tooltip>

        <h4 style="color: white" class="pa-1">{{ this.pages }}</h4>
        <v-tooltip top>
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              color="white"
              fab
              x-small
              text
              @click="inc"
              v-bind="attrs"
              v-on="on"
            >
              <v-icon> keyboard_arrow_right </v-icon>
            </v-btn>
          </template>
          <span>Next Page</span>
        </v-tooltip>
      </v-row>

      <v-toolbar v-if="select.length" dark>
        <v-btn icon @click="select = []">
          <v-icon>mdi-close</v-icon>
        </v-btn>

        <v-toolbar-title> {{ select.length }} Selected </v-toolbar-title>

        <v-spacer></v-spacer>

        <v-scale-transition>
          <v-btn v-if="select.length" key="delete" icon @click="deleteMails">
            <v-icon>mdi-delete</v-icon>
          </v-btn>
        </v-scale-transition>
      </v-toolbar>

      <v-list two-line>
        <v-list-item-group>
          <v-row no-gutters>
            <v-col>
              <v-card
                flat
                :key="project.mailHeader.date"
                v-for="(project, index) in emails"
              >
                <v-dialog v-model="removeTagDialogs[index]" width="800">
                  <template v-slot:activator="{ on, attrs }">
                    <v-list-item
                      :class="`pa-3 project ${project.mailHeader.Priority}`"
                      v-bind="attrs"
                      v-on="on"
                      @click="editData(project.mailHeader.Subject,project.mailHeader.Priority,project.messageBody.Body,project.mailHeader.Receivers,project.mailHeader.date,project.mailAttachements.attachements)"
                    >
                      <template>
                        <v-list-item-content>
                          <b>
                            <v-list-item-title
                              v-text="`Subject : ${project.mailHeader.Subject}`"
                            ></v-list-item-title
                          ></b>

                          <v-list-item-subtitle
                            class="text--primary"
                            v-text="`Sender : ${project.mailHeader.Sender}`"
                          ></v-list-item-subtitle>

                          <v-list-item-subtitle
                            v-text="
                              `Message : ${project.messageBody.Body.substring(
                                0,
                                5
                              )}....`
                            "
                          ></v-list-item-subtitle>
                        </v-list-item-content>

                        <v-list-item-action>
                          <v-list-item-action-text
                            v-text="
                              new Date(Number(project.mailHeader.date))
                                .toISOString()
                                .substring(0, 10)
                            "
                          ></v-list-item-action-text>

                          <v-list-item-action-text
                            v-text="
                              new Date(Number(project.mailHeader.date))
                                .toISOString()
                                .substring(11, 19)
                            "
                          ></v-list-item-action-text>
                        </v-list-item-action>
                      </template>
                    </v-list-item>
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

        <v-card-text>
          <v-form class="px-3" ref="form">
            <v-text-field
              label="To"
              v-model="To"
              prepend-icon="person"
              autocomplete="off"
              :rules="inputRules"
              hint="if you want to send for more than one type emails with , between them "
            ></v-text-field>

            <v-text-field
              label="Subject"
              v-model="Subject"
              prepend-icon="edit"
              autocomplete="off"
              :rules="inputRules"
            ></v-text-field>
            <v-textarea
              label="Message"
              v-model="content"
              prepend-icon="message"
              autocomplete="off"
              :rules="inputRules"
            ></v-textarea>
            <template>
                    <div v-for="(pt,i) in att" :key="pt" class="text-center">

                      <v-chip
                        class="ma-2"
                        color="indigo darken-3"
                        close
                        @click:close="att.splice(i,1)"
                        outlined
                      >
                        {{pt}} Attachement were selected before Drafting
                      </v-chip>
                    </div>
                  </template>
            <!--
            <v-btn fab text color="grey" class="ml-15" icon="close">
              <span>{{att.length}} Attachement(s) were selected before Drafting</span>
              <v-icon color="error">close</v-icon>
                                  
                        </v-btn>
                        -->
            
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

            <v-col cols="1">
              <v-card
                flat
                :key="project.mailHeader.date"
                v-for="project in emails"
                class="pa-5"
              >
                <v-checkbox v-model="select" :value="project"></v-checkbox>
              </v-card>
            </v-col>
          </v-row>
        </v-list-item-group>
      </v-list>
    </v-container>
  </div>
</template>



<script>
import axios from "axios";
import Navbar from "@/components/Navbar";
import {
  Mail,
  MessageBody,
  MailHeader,
  MailAttachements,
  MailID
} from "@/components/classes.js";

export default {
  name: "Draft",
  components: { Navbar },
  data() {
    return {
      To: "",
      
      Subject: "",
      content: "",
      priority: "low",
      att:null,
    
      date: new Date().toISOString(),
      inputRules: [
        (v) => !!v || "This field is required",
        (v) => v.length >= 1 || "Minimum length is 1 characters",
      ],
      radioGroup:'',
      rev:false,
      searchfor: "",
      Filerfor:'',
      elementfilter1: "",
      elementfilter2: "",
      sortFlag:true,
      Insearch:false,
      element: "",
      collapseOnScroll: false,
      removeTagDialogs: {},
      pages: 1,
      ss: "Date",
      item: "",
      select: [],
      username: this.$route.params.username,
      emails: null,
      mails: [],
      links: [{ text: "Sender" }, { text: "Subject" }, { text: "Priority" }],
      links2: [{ text: "Sender" }, { text: "Subject" }, { text: "SenderAndSubject" }],
    };
  },
  async mounted() {
    await this.sortGazzar('Date')
    //this.loadMails()
  },
  methods: {
    async refresh(){
      this.rev=false
      this.searchfor= ""
      this.radioGroup=''
      this.Filerfor=''
       this.sortFlag=true
       this.Insearch=false
       this.element= ""
       this.elementfilter1=""
      this.elementfilter2=""
       this.collapseOnScroll= false
       this.removeTagDialogs= {}
       this.pages= 1
       this.ss= "Date"
       this.item= ""
       this.select= []
      
       this.emails= null
       this.mails=[]
       await this.sortGazzar('Date')
       location.reload()
    },
    editData(sub,pri,body,p,date,attach){
      
      this.Subject=sub
      this.priority=pri
      this.content=body
      for(let i=0;i<p.length;i++){
          this.To += p[i]
          if(i!= (p.length -1)){
            this.To += ','
          }
      }
      this.date=date
      this.att=attach
      //this.$refs.myFileInput.reset();
    },
     clearPopup(){
       this.To = ""
        this.Subject = ""
        this.content = ""
        this.priority = "low"
    },
    close(p){
       this.removeTagDialogs[p]=false
      this.clearPopup()
      this.$refs.myFileInput.reset();
    },
     submit(p) {
      let array =this.To.split(',')
      if (this.$refs[`form`][0].validate()
) {
      var bodyFormData = new FormData();
      var s = null
      if( this.att!=null){
        s=this.att
        if(s.length==0){
          s=null
        }
      }

      console.log(s)
      bodyFormData.append("mail",JSON.stringify(new Mail(
                new MailAttachements(s),
                new MailHeader(
                  this.username,
                  array,
                  this.Subject,
                  this.date,
                  this.priority
                ),
                new MessageBody(this.content),
                new MailID(this.username,'draft',this.date))
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
                this.removeTagDialogs[p] = false;
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
         this.removeTagDialogs[p] = false;
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
                new MailID(this.username,'draft',this.date))
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
        this.removeTagDialogs[p] = false;
        //this.$refs.myFileInput.reset();
        //location.reload()
        this.refresh()
      }
      } ,
    async download(id, name) {
      await this.deleteatt(id, name);
      alert(
        "The Selected Attachement is Download in your downloads Directory ^_^"
      );
    },
    async sortBy(type) {
      this.ss = type;
      await this.sortGazzar(type);
    },
    async dec() {
      if (this.pages > 1) {
        if(this.Insearch==true){
          this.pages--;
        this.emails = null;
        }else{
          this.pages--;
        this.emails = null;
        await this.sortGazzar(this.ss);
        }
        
        //await this.load()
      }
    },
    async inc() {
      if(this.Insearch==true){
        this.pages++;
        this.emails = null;

      }else{
          this.pages++;
          this.emails = null;
          await this.sortGazzar(this.ss);
      }
      
      

      //await this.load()
    },

    async deleteMails() {
      for (let i = 0; i < this.select.length; i++) {
        await this.delete(this.select[i].mailID);
      }
      
      if(this.Insearch==false){
        this.select = [];
        this.emails = null;
        await this.load();
      }else{
        this.select = [];
        this.emails = null;
      }
      
      //location.reload()
    },
    async sortGazzar(p) {
      if(p=='Date'){
        this.rev=true
      }else if(p=='Priority'){
        this.rev=true
      }else if(p=='Subject'){
        this.rev=false
      }else if(p=='Sender'){
        this.rev=false
      }
      let response = await axios.get("http://localhost:8085/Sort", {
        params: {
          user: this.$route.params.username,
          sortType: p,
          folder: "draft",
          pageNum: this.pages,
          reversed: this.rev,
        },
      });
      if (response.status === 200) {
        this.emails = null;
        this.emails = await response.data;
      }
    },
    async deleteatt(id, name) {
      var bodyFormData = new FormData();
      bodyFormData.append("mailId", JSON.stringify(id));
      bodyFormData.append("fileName", name);
      axios({
        method: "post",
        url: "http://localhost:8085/downloadAttachment",
        data: bodyFormData,
        headers: { "Content-Type": "multipart/form-data" },
      });
    },
    async load() {
      this.select = [];
      let response = await axios.get("http://localhost:8085/loadMails", {
        params: {
          user: this.$route.params.username,
          folder: "draft",
          pageNum: this.pages,
        },
      });
      if (response.status === 200) {
        this.emails = await response.data;
      }
    },
    async delete(p) {
      await axios.post("http://localhost:8085/delete", JSON.stringify(p), {
        headers: {
          "Content-Type": "application/json",
        },
      });
    },
    show() {
      alert("hello");
    },
  },
};
</script>

<style>
.project.Superhigh {
  border-left: 4px solid rgb(255, 0, 0);
}
.project.high {
  border-left: 4px solid rgb(4, 0, 255);
}
.project.medium {
  border-left: 4px solid yellow;
}
.project.low {
  border-left: 4px solid green;
}
</style>
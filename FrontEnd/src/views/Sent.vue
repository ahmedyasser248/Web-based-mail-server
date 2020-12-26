<template>
  <div class="sent">
    <Navbar :username="username" navigation="Sent" />

    <v-row>
      <v-col cols="6">
        <v-app-bar class="mb-10" :collapse="!collapseOnScroll" absolute dark>
          <v-icon class="pr-3">mdi-magnify</v-icon>

          <v-text-field v-if="radioGroup=='Search'"
            placeholder="Type the word to be searched"
            v-model="element"
            hide-details
            single-line
          ></v-text-field>
          <v-text-field v-if="radioGroup=='Filter'"
            placeholder="Type the word to be filtered "
            v-model="elementfilter1"
            hide-details
            single-line
          ></v-text-field>
          <v-text-field v-if="radioGroup=='Filter' && Filterfor=='SenderAndSubject'"
            placeholder="Type the word to be filtered "
            v-model="elementfilter2"
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
                @click="ActualSearch"
                v-if="(Filterfor=='Sender' ||Filterfor=='Subject' ) && elementfilter1.length"
              >
                <v-icon color="success">done</v-icon>
              </v-btn>
              <v-btn
                fab
                text
                @click="ActualSearch"
                v-if="Filterfor=='SenderAndSubject' && elementfilter2.length"
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
           

          <v-menu offset-y dark color="white" hide-details>
            <template v-slot:activator="{ on, attrs }">
              <v-btn fab text color="grey" v-bind="attrs" v-on="on">
                <v-icon>expand_more</v-icon>
              </v-btn>
            </template>
            
            
              <v-menu offset-y dark color="white" hide-details>
              <template v-slot:activator="{ on, attrs }">
              <v-btn
                v-bind="attrs" v-on="on" @click="radioGroup='Search'" id="btn1"
              >Search</v-btn>
              </template>
              <v-list>
              <v-list-item
                v-for="link in links"
                :key="link.text"
                @click="searchstate(link.text)"
              >
                <v-list-item-title>{{ link.text }}</v-list-item-title>
              </v-list-item>
            </v-list>
            
              </v-menu>
              
              <v-menu offset-y dark color="white" hide-details>
              <template v-slot:activator="{ on, attrs }">
              <v-btn
                v-bind="attrs" v-on="on" @click="radioGroup='Filter'" id="btn2"
              >Filter</v-btn>
              </template>
              <v-list>
              `<v-list-item
                v-for="link in links2"
                :key="link.text"
                @click="filterstate(link.text)"
              >
                <v-list-item-title>{{ link.text }}</v-list-item-title>
              </v-list-item>
            </v-list>
              </v-menu>
            
          </v-menu>
        </v-app-bar>
      </v-col>
    </v-row>
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
                            v-text="`Receiver(s) : ${project.mailHeader.Receivers[0]}.....etc`"
                          >
                          </v-list-item-subtitle>

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
                      <div class="headline">
                        {{ project.mailHeader.Subject }}
                      </div>
                      <v-spacer></v-spacer>
                      <div
                        class="caption" 
                      >
                      <v-row v-for="rec in project.mailHeader.Receivers"
                            :key="rec">
                      <v-col v-text="` Receiver(s) : <${rec}>`">
                      </v-col>      
                      </v-row>
                      </div>
                    </v-card-title>

                    <v-card-text>
                      {{ project.messageBody.Body }}
                    </v-card-text>
                    <a
                      v-for="pic in project.mailAttachements.attachements"
                      :key="pic"
                      @click="download(project.mailID, pic)"
                    >
                      {{ pic }}
                      <v-divider></v-divider>
                    </a>

                    <v-divider></v-divider>

                    <v-card-actions>
                      <v-spacer></v-spacer>
                      <v-btn
                        color="success"
                        text
                        @click="removeTagDialogs[index] = false"
                      >
                        OK
                      </v-btn>
                    </v-card-actions>
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
/*import {
  Mail,
  MessageBody,
  MailHeader,
  MailAttachements,
} from "@/components/classes.js";*/

export default {
  name: "Sent",
  components: { Navbar },
  data() {
    return {
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
    },
    async undone() {
      let g=document.getElementById("btn1");
      g.disabled=false;
      let f=document.getElementById("btn2");
      f.disabled=false;
      this.radioGroup=''
      this.sortFlag=true
      this.Insearch=false
      this.select = [];
      this.searchfor=""
      this.Filerfor=''
      this.elementfilter1=""
      this.elementfilter2=""
      this.element = "";
      this.emails=null;
      this.collapseOnScroll = false;
      await this.sortGazzar('Date')
      //ocation.reload()
    },
    searchstate(p) {
      this.searchfor = p;
      this.collapseOnScroll = true;
      let g=document.getElementById("btn1");
      g.disabled=true;
      let f=document.getElementById("btn2");
      f.disabled=true;
    },
    filterstate(p) {
      this.Filterfor = p;
      this.collapseOnScroll = true;
      let g=document.getElementById("btn1");
      g.disabled=true;
      let f=document.getElementById("btn2");
      f.disabled=true;
    },
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
        await this.ActualSearch()
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
        await this.ActualSearch()

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
        await this.sortGazzar('Date')
      }else{
        this.select = [];
        this.emails = null;
        await this.ActualSearch()
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
          folder: "sent",
          pageNum: this.pages,
          reversed: this.rev,
        },
      });
      if (response.status === 200) {
        this.emails = null;
        this.emails = await response.data;
      }
    },
    async ActualSearch() {
      this.Insearch=true
      this.sortFlag=false
      if(this.radioGroup=='Search'){
      let response = await axios.get("http://localhost:8085/Search", {
        params: {
          user: this.$route.params.username,
          searchFor: this.searchfor,
          folder: "sent",
          pageNum: this.pages,
          element: this.element,
        },
      });
      if (response.status === 200) {
        if(await response.data.length==0){
          alert("The item you searched for is not found")
        }else{
          this.emails = null;
          this.emails = await response.data;
        }
      }
      }else if(this.radioGroup=='Filter'){

        let response = await axios.get("http://localhost:8085/Filter", {
        params: {
          user: this.$route.params.username,
          filterType: this.Filerfor,
          folder: "sent",
          pageNum: this.pages,
          firstFilter: this.elementfilter1,
          secondFilter:this.elementfilter2
        },
      });
      if (response.status === 200) {
        if(await response.data.length==0){
          alert("The item you searched for is not found")
        }else{
          this.emails = null;
          this.emails = await response.data;
        }
      }

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
          folder: "sent",
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
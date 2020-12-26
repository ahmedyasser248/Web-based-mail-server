<template> 
  <nav >
    <v-app-bar flat app dark >
      <!--three lines new -->
  
      
      <v-app-bar-nav-icon @click="drawer = !drawer" class="grey--text"></v-app-bar-nav-icon>
      <v-toolbar-title class="text-uppercase  red--text">
        <span class="font-weight-light grey--text">Mail</span>
        <span>Server</span>
      </v-toolbar-title>
      

      <!--
        <v-text-field placeholder="Type the word then select type of search" class=" ml-15"
        hide-details
        prepend-icon="mdi-magnify"
        single-line
      ></v-text-field>
      <v-btn fab x-small text>
               <v-icon>
                  expand_more
                  </v-icon> 
              </v-btn>
              -->

      <v-spacer></v-spacer>
      <v-toolbar-title class="text-uppercase  red--text">
        <span class="font-weight-light cyan--text text-center pr-15 mr-15">{{navigation}}</span>
      </v-toolbar-title>
      
      <!--dropdown menue-->
      <template>
        <div class="text-center" >
          <v-menu offset-y>
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                text
                color="grey"
                v-bind="attrs"
                v-on="on"
              >
                <v-icon left>expand_more</v-icon>
                <span>Menu</span>
              </v-btn>
            </template>
            <v-list>
            `<v-list-item  v-for="link in links" :key="link.text" router :to="link.route" >
              <v-list-item-action>
                <v-icon>{{link.icon}}</v-icon>
              </v-list-item-action>
              <v-list-item-title>{{link.text}}</v-list-item-title>
            </v-list-item>
            </v-list>
          </v-menu>
        </div>
    </template>


      <v-btn router :to="'/'" text color="grey" >
        <span>Sign Out</span>
        <v-icon right>exit_to_app</v-icon>
      </v-btn>
    </v-app-bar>

    <v-navigation-drawer app v-model="drawer" dark>
       <v-row class="text-center">
        <v-col class="mt-5">
         <v-avatar size="100" color="grey">
            <v-icon dark size="80">
              person
            </v-icon>
          </v-avatar>
          <p class="white--text subheading mt-1">{{username}}</p>
        </v-col>
       </v-row>
       <v-col>
        <Popup :username='username' />
       </v-col>
      <v-list>
        <v-list-item v-for="link in links" :key="link.text" router :to="link.route" >
            <v-list-item-action>
            <v-icon class="white--text">{{link.icon}}</v-icon>
            </v-list-item-action>
            <v-list-item-title class="white--text">{{link.text}}</v-list-item-title>
        </v-list-item>
        
      </v-list>
    </v-navigation-drawer>

  </nav>
</template>

<script>
import Popup from './Popup'
export default {
  components: {Popup},
  props :{username: String , navigation:String},
  data() {
    return {
      drawer: false,
      links: [
        { icon: 'inbox', text: 'Inbox', route: '/inbox/'+this.username },
        { icon: 'send', text: 'Sent', route: '/sent/' +this.username },
        { icon: 'delete', text: 'Trash', route: '/trash/'+this.username },
        { icon: 'mail', text: 'Draft', route: '/draft/'+this.username},
        { icon: 'person', text: 'Contact', route: '/contact/'+this.username },
      ]
    }
  },
}
</script>

<style>
.v-text__field{
  color: blue;
}
</style>

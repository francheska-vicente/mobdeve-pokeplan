package com.mobdeve.s11.pokeplan;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Users {
        private boolean[] userPokedex;

        private String fullName;
        private String email;
        private String userName;

        public Users(String fullName, String email, String userName) {
            userPokedex = new boolean[150];

            this.fullName = fullName;
            this.email = email;
            this.userName = userName;

        }

        public String getName () {
            return this.fullName;
        }

        public String getEmail () {
            return this.email;
        }

        public String getUserName () {
            return this.userName;
        }
}

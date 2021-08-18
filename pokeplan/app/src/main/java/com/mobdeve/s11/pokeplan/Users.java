package com.mobdeve.s11.pokeplan;


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


}

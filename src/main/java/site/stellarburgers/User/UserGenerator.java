package site.stellarburgers.User;

import com.github.javafaker.Faker;

import java.util.Locale;

public class UserGenerator {
 static Faker faker = new Faker();

 public static User getUserData() {
  final String email = faker.name().firstName() + "@ya.ya";
  final String password = faker.rickAndMorty().character();
  final String name = faker.name().firstName();
  return new User(email, password, name);
 }

 public static User getUserWithoutEmail() {
  final String password = faker.rickAndMorty().character();
  final String name = faker.name().firstName();
  return new User(null, password, name);
 }
 public static User getUserWithoutPassword(){
  final String email = faker.rickAndMorty().character() + "@ya.ya";
  final String name = faker.name().firstName();
  return new User(email, null, name);
}
 public static User getUserWithoutName() {
  final String email = faker.rickAndMorty().character() + "@ya.ya";
  final String password = faker.rickAndMorty().character();
  return new User(email, password, null );
 }
 public static User updateUserName(){
  final String name = faker.funnyName().name();
  return new User(null,null, name );
 }
 public static User updateUserEmail(){
  final String email = faker.funnyName().name() + "@ya.ya";
  return new User(email,null,null);
 }
 public static User updateUserPassword(){
  final String password = faker.rickAndMorty().character();
  return new User(null, password,null);
 }
 public static User userWithSameEmail(){
  final String email = "iamacopy@ya.ya";
  return new User(email, null, null);
 }
}
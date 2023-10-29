import { User as UserDetails } from "../types/user.type";
import { NextFunction, Request, Response } from "express";
import { db } from "../server";
import { ObjectId, WithId } from "mongodb";
import HTTP from "../utils/statusCodeConfig";
import jwt from "jsonwebtoken";
import Bcrypt from "../utils/bcrypt";
import CustomError from "../utils/CustomError";

export default class Auth {
  private bcrypt: Bcrypt = new Bcrypt();

  constructor() {}

  async signup(req: Request, res: Response, next: NextFunction) {
    const userData = req.body;

    try {
      validateSignupData(userData);
      const hashedPassword = await this.bcrypt.hashPassword(userData.password);
      await this.insertUser(userData, hashedPassword);
      res.status(HTTP.OK).json({ message: "You're signed up. Please login." });
    } catch (error) {
      return next(error);
    }
  }

  async login(req: Request, res: Response, next: NextFunction) {
    const { username, password } = req.body;

    try {
      const user = await getUserByUsername(username);
      validateUserLogin(user, password);
      const token = generateToken(user._id);
      res.status(HTTP.OK).json({ message: "Logged in.", token });
    } catch (error) {
      return next(error);
    }
  }

  // Other methods...

  private async insertUser(userData: any, hashedPassword: string) {
    await db.collection("Users").insertOne({
      username: userData.username,
      password: hashedPassword,
      role: "user",
      email: userData.email,
      phone_number: userData.phone_number,
      emergency_contact: userData.emergency_contact,
      created_at: new Date(),
      updated_at: new Date(),
    });
  }

  // Other extracted functions...

  private generateToken(userId: ObjectId): string {
    return jwt.sign({ userId }, process.env.JWT_SECRET!, {
      expiresIn: "7d",
    });
  }

  private async getUserByUsername(username: string): Promise<WithId<UserDetails>> {
    return await db.collection("Users").findOne({ username });
  }

  private validateSignupData(userData: any): void {
    if (
      !userData.username ||
      !userData.email ||
      !userData.password ||
      !userData.confirm_password ||
      !userData.phone_number ||
      !userData.emergency_contact
    ) {
      throw new CustomError(
        "Users must provide 'username', 'email', 'password', 'confirm_password', 'phone_number', 'emergency_contact' field.",
        HTTP.BAD_REQUEST
      );
    }

    if (userData.phone_number.length !== 10) {
      throw new CustomError("Invalid phone number", HTTP.BAD_REQUEST);
    }

    if (userData.password !== userData.confirm_password) {
      throw new CustomError("Password fields do not match", HTTP.BAD_REQUEST);
    }
  }

  private validateUserLogin(user: WithId<UserDetails>, password: string): void {
    if (!user || !this.bcrypt.checkPassword(user.password, password)) {
      throw new CustomError("Username or Password does not match", HTTP.UNAUTHORIZED);
    }
  }
}

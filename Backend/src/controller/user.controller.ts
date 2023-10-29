import { User as UserDetails } from "../types/user.type";
import { NextFunction, Request, Response } from "express";
import { db } from "../server";
import { ObjectId, WithId } from "mongodb";
import HTTP from "../utils/statusCodeConfig";

function sendJSONResponse(res: Response, statusCode: number, data: any) {
  res.status(statusCode).json(data);
}

export default class User {
  constructor() {}

  async getUsers(req: Request, res: Response, next: NextFunction) {
    const users = await this.fetchUsers();
    this.respondWithJSON(res, HTTP.OK, users);
  }

  async getProfile(req: Request, res: Response, next: NextFunction) {
    const user = await this.fetchUserProfile(req);
    this.respondWithJSON(res, HTTP.OK, {
      ...user,
      image: this.getUserImageUrl(user.username),
    });
  }

  async uploadImage(req: Request, res: Response, next: NextFunction) {
    this.respondWithJSON(res, HTTP.OK, {
      message: "Image uploaded",
    });
  }

  private async fetchUsers(): Promise<WithId<UserDetails>[]> {
    return await db.collection("Users").find().toArray();
  }

  private async fetchUserProfile(req: Request): Promise<WithId<UserDetails>> {
    const userId = (req as any).user._id;
    return await db.collection("Users").findOne({ _id: new ObjectId(userId) }, {
      projection: {
        username: 1,
        phone_number: 1,
        email: 1,
        emergency_contact: 1,
      },
    });
  }

  private respondWithJSON(res: Response, statusCode: number, data: any) {
    sendJSONResponse(res, statusCode, data);
  }

  private getUserImageUrl(username: string): string {
    return process.env.MINIO_IMAGE_URL!.replace("%USERNAME%", username);
  }
}

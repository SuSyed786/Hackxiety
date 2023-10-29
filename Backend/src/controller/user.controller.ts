/**
 * REFACTORING AND STRICT TYPE CHECK REQUIRED
 */
import {User as UserDetails} from "../types/user.type";

import {NextFunction, Request, Response} from "express";
import {db} from "../server";
import {ObjectId, WithId} from "mongodb";
import HTTP from "../utils/statusCodeConfig";
function sendJSONResponse(res: Response, statusCode: number, data: any) {
  res.status(statusCode).json(data);
}
export default class User {
  constructor() {}
  
  async getUsers(req: Request, res: Response, next: NextFunction) {
    const users = <WithId<UserDetails>[]>(
      await db.collection("Users").find().toArray()
    );
    sendJSONResponse(res, HTTP.OK, users);
  }

  async getProfile(req: Request, res: Response, next: NextFunction) {
    const user = <WithId<UserDetails>>await db.collection("Users").findOne(
      {_id: new ObjectId((req as any).user._id)},
      {
        projection: {
          username: 1,
          phone_number: 1,
          email: 1,
          emergency_contact: 1,
        },
      }
    );
    sendJSONResponse(res, HTTP.OK, {
      ...user,
      image: process.env.MINIO_IMAGE_URL!.replace("%USERNAME%", user.username),
    });
  }

  async uploadImage(req: Request, res: Response, next: NextFunction) {
    sendJSONResponse(res, HTTP.OK, {
      message: "Image uploaded",
    });
  }
}

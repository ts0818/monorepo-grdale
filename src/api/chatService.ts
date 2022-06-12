import request from "@/api/request";

class ChatService {
  sendMessage(data: any) {
    return request({
      method: "post",
      url: "/chat/message",
      data: data,
      responseType: "json",
    });
  }
}
export default new ChatService();

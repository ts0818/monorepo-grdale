interface ChatUserModelType {
  userId: string;
  userName: string;
  messageType: string;
  content: string;
  chatMessageId: any;
  chatThreadId: any;
}

class ChatUserModel implements ChatUserModelType {
  userId = "";
  userName = "";
  messageType = "";
  content = "";
  chatMessageId = "";
  chatThreadId = "";

  constructor(init?: Partial<ChatUserModel>) {
    Object.assign(this, init);
  }
}
export default new ChatUserModel();

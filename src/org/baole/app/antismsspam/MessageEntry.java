package org.baole.app.antismsspam;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageEntry implements Parcelable {
	public static final int TYPE_INBOX = 1;
	public static final int TYPE_SENT = 2;
	public static final int TYPE_DRAFT = 3;
	public static final int TYPE_OUTBOX = 4;
	
	public String body;
	public String address;
	public long created;
	public int type;
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel par, int flags) {
		par.writeString(body);
		par.writeString(address);
		par.writeLong(created);
		par.writeInt(type);
	}

	public static final Parcelable.Creator<MessageEntry> CREATOR = new Parcelable.Creator<MessageEntry>() {
		public MessageEntry createFromParcel(Parcel in) {
			return new MessageEntry(in);
		}

		public MessageEntry[] newArray(int size) {
			return new MessageEntry[size];
		}
	};

	private MessageEntry(Parcel in) {
//		id = in.readLong();
		body = in.readString();
		address = in.readString();
		created = in.readLong();
		type = in.readInt();
	}

	public MessageEntry() {
	}

	@Override
	public String toString() {
		return address + " : " + body;
	}
	

}

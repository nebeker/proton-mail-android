/*
 * Copyright (c) 2020 Proton Technologies AG
 * 
 * This file is part of ProtonMail.
 * 
 * ProtonMail is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ProtonMail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ProtonMail. If not, see https://www.gnu.org/licenses/.
 */
package ch.protonmail.android.contacts.list.listView

import android.database.Cursor
import android.provider.ContactsContract

/**
 * Created by Kamil Rajtar on 23.08.18.  */
class ContactItemListFactory {

	private fun Cursor.getStringByName(name:String)=getString(getColumnIndex(name))


	private fun Cursor.extractContactItem():ContactItem {
		val contactId=getStringByName(ContactsContract.CommonDataKinds.Email.RAW_CONTACT_ID)
		val name=getStringByName(ContactsContract.CommonDataKinds.Email.DISPLAY_NAME_PRIMARY)
		val email=getStringByName(ContactsContract.CommonDataKinds.Email.ADDRESS)
		return ContactItem(false,
				contactId,
				name,
				email,
				0)
	}

	fun convert(cursor:Cursor):List<ContactItem> {
		val contactsMap=mutableMapOf<String,ContactItem>()
		val contactsList=mutableListOf<ContactItem>()
		cursor.apply {
			while(moveToNext()) {
				val contactItem=extractContactItem()
				val contactId=contactItem.contactId ?: continue
				val item=contactsMap[contactId]
				if(item!=null) {
					item.additionalEmailsCount=item.additionalEmailsCount+1
				} else {
					contactsMap[contactId]=contactItem
					contactsList.add(contactItem)
				}
			}
		}

		return contactsList
	}
}

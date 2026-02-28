/*
 *  Warpnet Android
 *
 *  Copyright (C) WarpnetProject and Contributors
 *
 *  This file is part of Warpnet Android.
 *
 *  Warpnet Android is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Warpnet Android is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Warpnet Android. If not, see <http://www.gnu.org/licenses/>.
 */
package com.warpnet.services.warpnet.model.fields

enum class Expansions(val value: String) {
  attachments_poll_ids("attachments.poll_ids"),
  attachments_media_keys("attachments.media_keys"),
  author_id("author_id"),
  geo_place_id("geo.place_id"),
  in_reply_to_user_id("in_reply_to_user_id"),
  referenced_tweets_id("referenced_tweets.id"),
  entities_mentions_username("entities.mentions.username"),
  referenced_tweets_id_author_id("referenced_tweets.id.author_id"),
}

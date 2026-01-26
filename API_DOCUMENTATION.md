# Social Network API Documentation

## Posts API

### Get All Posts
```
GET /api/posts?page=0&size=10&sortBy=createdAt&sortDir=desc
```
- **Description**: Get all posts with pagination
- **Authentication**: Not required
- **Response**: Page of PostResponse objects

### Get My Posts
```
GET /api/posts/my?page=0&size=10&sortBy=createdAt&sortDir=desc
```
- **Description**: Get current user's posts only
- **Authentication**: Required
- **Response**: Page of PostResponse objects

### Create Post
```
POST /api/posts
Content-Type: application/json

{
  "content": "This is my post content"
}
```
- **Description**: Create a new post
- **Authentication**: Required
- **Response**: Created PostResponse (201)

### Update Post
```
PUT /api/posts/{id}
Content-Type: application/json

{
  "content": "Updated post content"
}
```
- **Description**: Update post content
- **Authentication**: Required (Author only)
- **Response**: Updated PostResponse

### Delete Post
```
DELETE /api/posts/{id}
```
- **Description**: Delete a post
- **Authentication**: Required (Author or Admin)
- **Response**: 204 No Content

### Get Post by ID
```
GET /api/posts/{id}
```
- **Description**: Get single post with full details
- **Authentication**: Not required
- **Response**: PostResponse object

## Likes API

### Toggle Like
```
POST /api/posts/{postId}/like
```
- **Description**: Toggle like/unlike a post
- **Authentication**: Required
- **Response**: 
```json
{
  "liked": true,
  "likeCount": 5
}
```

### Get Users Who Liked Post
```
GET /api/posts/{postId}/likes?page=0&size=10&sortBy=likedAt&sortDir=desc
```
- **Description**: Get list of users who liked the post
- **Authentication**: Not required
- **Response**: List of LikedUserResponse objects

### Get Users Who Liked Post (Paginated)
```
GET /api/posts/{postId}/likes/paginated?page=0&size=10&sortBy=likedAt&sortDir=desc
```
- **Description**: Get paginated list of users who liked the post
- **Authentication**: Not required
- **Response**: Page of LikedUserResponse objects

### Check if User Liked Post
```
GET /api/posts/{postId}/liked
```
- **Description**: Check if current user liked the post
- **Authentication**: Required
- **Response**: Boolean (true/false)

### Get Like Count
```
GET /api/posts/{postId}/like-count
```
- **Description**: Get total like count for a post
- **Authentication**: Not required
- **Response**: Long (like count)

## Response Models

### PostResponse
```json
{
  "id": 1,
  "content": "Post content",
  "author": {
    "id": 1,
    "username": "user1",
    "email": "user1@example.com",
    "avatarPath": "/avatars/user1.jpg"
  },
  "likeCount": 5,
  "createdAt": "2026-01-26T15:48:58",
  "updatedAt": "2026-01-26T15:48:58",
  "isLikedByCurrentUser": true
}
```

### LikedUserResponse
```json
{
  "id": 1,
  "username": "user1",
  "email": "user1@example.com",
  "avatarPath": "/avatars/user1.jpg",
  "likedAt": "2026-01-26T15:48:58"
}
```

### LikeResponse
```json
{
  "liked": true,
  "likeCount": 5
}
```

## Error Responses

### 404 Not Found
```json
{
  "timestamp": "2026-01-26T15:48:58.123+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Post not found with id: 1",
  "path": "/api/posts/1"
}
```

### 400 Bad Request
```json
{
  "timestamp": "2026-01-26T15:48:58.123+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Content must be between 1 and 1000 characters",
  "path": "/api/posts"
}
```

### 403 Forbidden
```json
{
  "timestamp": "2026-01-26T15:48:58.123+00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "You can only edit your own posts",
  "path": "/api/posts/1"
}
```

## Features

- **Thread Safety**: All like operations are thread-safe using Spring's @Transactional
- **Pagination**: All list endpoints support pagination
- **Sorting**: Sortable by multiple fields with ascending/descending options
- **Authentication**: Spring Security integration with role-based access control
- **Validation**: Input validation with proper error messages
- **Performance**: Optimized queries with eager loading where needed

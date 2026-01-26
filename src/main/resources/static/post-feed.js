// Post Feed JavaScript - Matches existing design patterns
console.log('PostFeed script loaded');
class PostFeed {
    constructor() {
        console.log('PostFeed constructor called');
        this.posts = [];
        this.currentPage = 0;
        this.pageSize = 10;
        this.isLoading = false;
        this.viewMode = 'compact'; // 'compact' or 'card'
        this.init();
    }

    init() {
        console.log('PostFeed init method called');
        this.loadPosts();
        this.setupCreatePostForm();
        this.setupViewModeToggle();
        this.loadViewModePreference();
        // Removed infinite scroll and auto-refresh for performance
    }

    setupCreatePostForm() {
        const textarea = document.getElementById('createPostTextarea');
        const submitBtn = document.getElementById('createPostBtn');
        const charCounter = document.getElementById('charCounter');
        const fileInput = document.getElementById('fileInput');
        const filePreview = document.getElementById('filePreview');

        if (textarea && submitBtn) {
            // Character counter
            textarea.addEventListener('input', () => {
                const length = textarea.value.length;
                const maxLength = 1000;
                
                if (charCounter) {
                    charCounter.textContent = `${length}/${maxLength}`;
                    charCounter.className = 'char-counter';
                    
                    if (length > maxLength * 0.9) {
                        charCounter.classList.add('danger');
                    } else if (length > maxLength * 0.7) {
                        charCounter.classList.add('warning');
                    }
                }
                
                submitBtn.disabled = length === 0 || length > maxLength;
            });

            // File input handling
            if (fileInput) {
                fileInput.addEventListener('change', (e) => {
                    this.handleFileSelect(e.target.files);
                });
            }

            // Submit form
            submitBtn.addEventListener('click', () => {
                this.createPost(textarea.value, fileInput ? fileInput.files : []);
            });

            // Enter key to submit (with Shift+Enter for new line)
            textarea.addEventListener('keydown', (e) => {
                if (e.key === 'Enter' && !e.shiftKey) {
                    e.preventDefault();
                    if (!submitBtn.disabled) {
                        this.createPost(textarea.value, fileInput ? fileInput.files : []);
                    }
                }
            });
        }
    }

    async createPost(content, files = []) {
        const textarea = document.getElementById('createPostTextarea');
        const submitBtn = document.getElementById('createPostBtn');
        const fileInput = document.getElementById('fileInput');

        if (!content.trim()) return;

        // Disable button during request
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<i class="bi bi-hourglass-split me-2"></i>Posting...';

        try {
            let response;
            
            if (files.length > 0) {
                // Use multipart/form-data for posts with files
                const formData = new FormData();
                formData.append('content', content.trim());
                
                for (let i = 0; i < files.length; i++) {
                    formData.append('files', files[i]);
                }
                
                response = await fetch('/api/posts/with-media', {
                    method: 'POST',
                    body: formData
                });
            } else {
                // Use regular JSON for posts without files
                response = await fetch('/api/posts', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ content: content.trim() })
                });
            }

            if (response.ok) {
                const newPost = await response.json();
                this.posts.unshift(newPost);
                this.renderPosts();
                
                // Clear form
                textarea.value = '';
                if (fileInput) fileInput.value = '';
                this.clearFilePreview();
                
                const charCounter = document.getElementById('charCounter');
                if (charCounter) {
                    charCounter.textContent = '0/1000';
                    charCounter.className = 'char-counter';
                }
                
                // Show success message
                this.showNotification('Post created successfully!', 'success');
            } else {
                throw new Error('Failed to create post');
            }
        } catch (error) {
            console.error('Error creating post:', error);
            this.showNotification('Failed to create post. Please try again.', 'error');
        } finally {
            submitBtn.disabled = false;
            submitBtn.innerHTML = '<i class="bi bi-send me-2"></i>Post';
        }
    }

    handleFileSelect(files) {
        const filePreview = document.getElementById('filePreview');
        if (!filePreview) return;

        this.clearFilePreview();
        
        for (let i = 0; i < files.length; i++) {
            const file = files[i];
            
            // Validate file type
            if (!this.isValidImageFile(file)) {
                this.showNotification(`${file.name} is not a valid image file. Only JPEG, PNG, GIF, and WebP are allowed.`, 'error');
                continue;
            }
            
            // Validate file size (10MB max)
            if (file.size > 10 * 1024 * 1024) {
                this.showNotification(`${file.name} is too large. Maximum file size is 10MB.`, 'error');
                continue;
            }
            
            // Create preview
            const reader = new FileReader();
            reader.onload = (e) => {
                const previewItem = document.createElement('div');
                previewItem.className = 'file-preview-item';
                previewItem.innerHTML = `
                    <img src="${e.target.result}" alt="${file.name}">
                    <div class="file-preview-info">
                        <div class="file-name">${file.name}</div>
                        <div class="file-size">${this.formatFileSize(file.size)}</div>
                        <button type="button" class="btn-remove-file" onclick="postFeed.removeFilePreview(this)">
                            <i class="bi bi-x"></i>
                        </button>
                    </div>
                `;
                filePreview.appendChild(previewItem);
            };
            reader.readAsDataURL(file);
        }
    }

    clearFilePreview() {
        const filePreview = document.getElementById('filePreview');
        if (filePreview) {
            filePreview.innerHTML = '';
        }
    }

    removeFilePreview(button) {
        const previewItem = button.closest('.file-preview-item');
        if (previewItem) {
            previewItem.remove();
        }
        
        // Clear file input if no more files
        const filePreview = document.getElementById('filePreview');
        const fileInput = document.getElementById('fileInput');
        if (filePreview && filePreview.children.length === 0 && fileInput) {
            fileInput.value = '';
        }
    }

    isValidImageFile(file) {
        const validTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif', 'image/webp'];
        return validTypes.includes(file.type);
    }

    formatFileSize(bytes) {
        if (bytes === 0) return '0 Bytes';
        const k = 1024;
        const sizes = ['Bytes', 'KB', 'MB', 'GB'];
        const i = Math.floor(Math.log(bytes) / Math.log(k));
        return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    }

    async loadPosts() {
        if (this.isLoading) return;

        this.isLoading = true;
        this.showLoadingSpinner();

        try {
            const response = await fetch(`/api/posts?page=${this.currentPage}&size=${this.pageSize}&sortBy=createdAt&sortDir=desc`);
            
            if (response.ok) {
                const data = await response.json();
                
                if (this.currentPage === 0) {
                    this.posts = data.content;
                } else {
                    // Simply add new posts - no merging needed without auto-refresh
                    this.posts.push(...data.content);
                }
                
                this.renderPosts();
                this.currentPage++;
                
                // Hide load more button if no more posts
                if (data.last) {
                    this.hideLoadMoreButton();
                } else {
                    this.showLoadMoreButton();
                }
            } else {
                throw new Error('Failed to load posts');
            }
        } catch (error) {
            console.error('Error loading posts:', error);
            this.showNotification('Failed to load posts. Please refresh the page.', 'error');
        } finally {
            this.isLoading = false;
            this.hideLoadingSpinner();
        }
    }

    renderPosts() {
        const container = document.getElementById('postsContainer');
        if (!container) return;

        if (this.posts.length === 0) {
            container.innerHTML = `
                <div class="empty-feed">
                    <i class="bi bi-chat-square-text"></i>
                    <h3>No posts yet</h3>
                    <p>Be the first to share something with the community!</p>
                </div>
            `;
            return;
        }

        container.innerHTML = this.posts.map(post => this.renderPost(post)).join('');
    }

    renderPost(post) {
        const createdAt = new Date(post.createdAt).toLocaleString();
        const authorInitial = post.author.username.charAt(0).toUpperCase();
        
        const isLikedClass = post.isLikedByCurrentUser ? 'liked' : '';
        const heartIcon = post.isLikedByCurrentUser ? 'bi-heart-fill' : 'bi-heart';
        
        const mediaHtml = this.renderMediaFiles(post.mediaFiles);
        
        return `
            <div class="post-item" data-post-id="${post.id}">
                <div class="post-header">
                    <div class="post-author-avatar">${authorInitial}</div>
                    <div class="post-author-info">
                        <div class="post-author-name">${post.author.username}</div>
                        <div class="post-time">${createdAt}</div>
                    </div>
                    ${this.canEditPost(post) ? this.renderEditDeleteButtons(post.id) : ''}
                </div>
                <div class="post-content">${this.escapeHtml(post.content)}</div>
                ${mediaHtml}
                <div class="post-actions">
                    <button class="post-action-btn ${isLikedClass}" 
                            onclick="postFeed.toggleLike(${post.id})">
                        <i class="bi ${heartIcon}"></i>
                        <span class="like-count">${post.likeCount}</span>
                    </button>
                </div>
            </div>
        `;
    }

    renderMediaFiles(mediaFiles) {
        if (!mediaFiles || mediaFiles.length === 0) {
            return '';
        }
        
        const mediaItems = mediaFiles.map(media => {
            return `
                <div class="post-media-item">
                    <img src="${media.fileUrl}" alt="${media.originalFileName}" 
                         class="post-media-image" 
                         onclick="postFeed.viewMediaImage('${media.fileUrl}', '${media.originalFileName}')"
                         loading="lazy">
                </div>
            `;
        }).join('');
        
        return `
            <div class="post-media-container">
                ${mediaItems}
            </div>
        `;
    }

    viewMediaImage(imageUrl, imageName) {
        // Create modal for viewing full-size image
        const modal = document.createElement('div');
        modal.className = 'image-viewer-modal';
        modal.innerHTML = `
            <div class="image-viewer-backdrop" onclick="postFeed.closeImageViewer()"></div>
            <div class="image-viewer-content">
                <button class="btn-close-image-viewer" onclick="postFeed.closeImageViewer()">
                    <i class="bi bi-x"></i>
                </button>
                <img src="${imageUrl}" alt="${imageName}" class="viewer-image">
                <div class="viewer-image-name">${imageName}</div>
            </div>
        `;
        
        document.body.appendChild(modal);
        document.body.style.overflow = 'hidden';
        
        // Close on Escape key
        const handleEscape = (e) => {
            if (e.key === 'Escape') {
                this.closeImageViewer();
                document.removeEventListener('keydown', handleEscape);
            }
        };
        document.addEventListener('keydown', handleEscape);
    }

    closeImageViewer() {
        const modal = document.querySelector('.image-viewer-modal');
        if (modal) {
            modal.remove();
            document.body.style.overflow = '';
        }
    }

    renderEditDeleteButtons(postId) {
        return `
            <div class="post-edit-delete-actions">
                <button class="btn-post-edit" onclick="postFeed.editPost(${postId})">
                    <i class="bi bi-pencil"></i> Edit
                </button>
                <button class="btn-post-delete" onclick="postFeed.deletePost(${postId})">
                    <i class="bi bi-trash"></i> Delete
                </button>
            </div>
        `;
    }

    async toggleLike(postId) {
        const button = document.querySelector(`[data-post-id="${postId}"] .post-action-btn`);
        const likeCount = button.querySelector('.like-count');
        const icon = button.querySelector('i');
        
        // Optimistic UI update
        const wasLiked = button.classList.contains('liked');
        const currentCount = parseInt(likeCount.textContent);
        
        if (wasLiked) {
            button.classList.remove('liked');
            icon.className = 'bi bi-heart';
            likeCount.textContent = currentCount - 1;
        } else {
            button.classList.add('liked');
            icon.className = 'bi bi-heart-fill';
            likeCount.textContent = currentCount + 1;
        }

        try {
            const response = await fetch(`/api/posts/${postId}/like`, {
                method: 'POST',
                headers: {}
            });

            if (response.ok) {
                const result = await response.json();
                
                // Update the post in our array with server data
                const postIndex = this.posts.findIndex(p => p.id === postId);
                if (postIndex !== -1) {
                    this.posts[postIndex].likeCount = result.likeCount;
                    this.posts[postIndex].isLikedByCurrentUser = result.liked;
                }
                
                // Update UI to match server state
                if (result.liked) {
                    button.classList.add('liked');
                    icon.className = 'bi bi-heart-fill';
                } else {
                    button.classList.remove('liked');
                    icon.className = 'bi bi-heart';
                }
                likeCount.textContent = result.likeCount;
            } else {
                // Revert optimistic update on error
                if (wasLiked) {
                    button.classList.add('liked');
                    icon.className = 'bi bi-heart-fill';
                    likeCount.textContent = currentCount;
                } else {
                    button.classList.remove('liked');
                    icon.className = 'bi bi-heart';
                    likeCount.textContent = currentCount;
                }
                throw new Error('Failed to toggle like');
            }
        } catch (error) {
            console.error('Error toggling like:', error);
            this.showNotification('Failed to update like. Please try again.', 'error');
        }
    }

    async editPost(postId) {
        const postElement = document.querySelector(`[data-post-id="${postId}"]`);
        const contentElement = postElement.querySelector('.post-content');
        const currentContent = contentElement.textContent.trim();

        const newContent = prompt('Edit your post:', currentContent);
        if (newContent === null || newContent.trim() === currentContent) return;

        if (newContent.trim().length === 0) {
            this.showNotification('Post content cannot be empty.', 'error');
            return;
        }

        try {
            const response = await fetch(`/api/posts/${postId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ content: newContent.trim() })
            });

            if (response.ok) {
                const updatedPost = await response.json();
                const postIndex = this.posts.findIndex(p => p.id === postId);
                if (postIndex !== -1) {
                    this.posts[postIndex] = updatedPost;
                    this.renderPosts();
                }
                this.showNotification('Post updated successfully!', 'success');
            } else {
                throw new Error('Failed to update post');
            }
        } catch (error) {
            console.error('Error updating post:', error);
            this.showNotification('Failed to update post. Please try again.', 'error');
        }
    }

    async deletePost(postId) {
        if (!confirm('Are you sure you want to delete this post?')) return;

        try {
            const response = await fetch(`/api/posts/${postId}`, {
                method: 'DELETE',
                headers: {}
            });

            if (response.ok) {
                this.posts = this.posts.filter(p => p.id !== postId);
                this.renderPosts();
                this.showNotification('Post deleted successfully!', 'success');
            } else {
                throw new Error('Failed to delete post');
            }
        } catch (error) {
            console.error('Error deleting post:', error);
            this.showNotification('Failed to delete post. Please try again.', 'error');
        }
    }

    canEditPost(post) {
        // This should be implemented based on current user
        // For now, we'll assume the user can edit their own posts
        return true; // This should check against current user's ID
    }

    showLoadingSpinner() {
        const container = document.getElementById('postsContainer');
        if (container) {
            container.innerHTML += `
                <div class="loading-spinner">
                    <i class="bi bi-arrow-clockwise"></i>
                </div>
            `;
        }
    }

    hideLoadingSpinner() {
        const spinner = document.querySelector('.loading-spinner');
        if (spinner) {
            spinner.remove();
        }
    }

    showLoadMoreButton() {
        let loadMoreBtn = document.getElementById('loadMoreBtn');
        if (!loadMoreBtn) {
            loadMoreBtn = document.createElement('button');
            loadMoreBtn.id = 'loadMoreBtn';
            loadMoreBtn.className = 'btn btn-outline-primary w-100 mt-3';
            loadMoreBtn.innerHTML = '<i class="bi bi-arrow-down-circle me-2"></i>Load More Posts';
            loadMoreBtn.addEventListener('click', () => this.loadPosts());
            
            const container = document.getElementById('postsContainer');
            if (container) {
                container.parentNode.appendChild(loadMoreBtn);
            }
        }
        loadMoreBtn.style.display = 'block';
    }

    hideLoadMoreButton() {
        const loadMoreBtn = document.getElementById('loadMoreBtn');
        if (loadMoreBtn) {
            loadMoreBtn.style.display = 'none';
        }
    }

    setupInfiniteScroll() {
        let timeout;
        window.addEventListener('scroll', () => {
            clearTimeout(timeout);
            timeout = setTimeout(() => {
                if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 1000) {
                    this.loadPosts();
                }
            }, 100);
        });
    }

    showNotification(message, type) {
        // Create notification element
        const notification = document.createElement('div');
        notification.className = `alert alert-${type === 'error' ? 'danger' : 'success'} alert-dismissible fade show position-fixed`;
        notification.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
        notification.innerHTML = `
            <i class="bi bi-${type === 'error' ? 'exclamation-triangle' : 'check-circle'} me-2"></i>
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        
        document.body.appendChild(notification);
        
        // Auto-remove after 5 seconds
        setTimeout(() => {
            if (notification.parentNode) {
                notification.remove();
            }
        }, 5000);
    }

    getCSRFToken() {
        return {
            token: '',
            header: 'X-CSRF-TOKEN'
        };
    }

    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    setupViewModeToggle() {
        const compactBtn = document.getElementById('compactViewBtn');
        const cardBtn = document.getElementById('cardViewBtn');

        if (compactBtn && cardBtn) {
            compactBtn.addEventListener('click', () => {
                this.setViewMode('compact');
            });

            cardBtn.addEventListener('click', () => {
                this.setViewMode('card');
            });
        }
    }

    setViewMode(mode) {
        this.viewMode = mode;
        
        // Update button states
        const compactBtn = document.getElementById('compactViewBtn');
        const cardBtn = document.getElementById('cardViewBtn');
        
        if (compactBtn && cardBtn) {
            compactBtn.classList.toggle('active', mode === 'compact');
            cardBtn.classList.toggle('active', mode === 'card');
        }

        // Update post items
        const postItems = document.querySelectorAll('.post-item');
        postItems.forEach(item => {
            item.classList.toggle('compact', mode === 'compact');
        });

        // Save preference to localStorage
        localStorage.setItem('postFeedViewMode', mode);
    }

    loadViewModePreference() {
        const savedMode = localStorage.getItem('postFeedViewMode');
        if (savedMode && (savedMode === 'compact' || savedMode === 'card')) {
            this.setViewMode(savedMode);
        }
    }
}

// Initialize post feed when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM loaded, creating postFeed instance');
    window.postFeed = new PostFeed();
});

class AIChatWidget {
    constructor() {
        this.sessionId = this.generateSessionId();
        this.isProcessing = false;
        this.init();
    }

    init() {
        this.addMessage('Hello! I\'m your ShopOnline assistant. I can help you with orders, products, and answer any questions about our store.', 'ai');

        document.getElementById('aiChatForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            await this.handleUserMessage();
        });

        document.getElementById('aiMessageInput').addEventListener('keypress', (e) => {
            if (e.key === 'Enter' && !e.shiftKey && !this.isProcessing) {
                e.preventDefault();
                this.handleUserMessage();
            }
        });
    }

    async handleUserMessage() {
        if (this.isProcessing) return;

        const input = document.getElementById('aiMessageInput');
        const message = input.value.trim();

        if (!message) return;

        this.addMessage(message, 'user');
        input.value = '';
        this.setProcessingState(true);
        this.showTypingIndicator();

        try {
            const response = await this.sendMessageToServer(message);
            this.hideTypingIndicator();

            if (response.success) {
                this.addMessage(response.response, 'ai');
            } else {
                this.addMessage('Sorry, I encountered an error. Please try again.', 'ai');
            }
        } catch (error) {
            console.error('Chat error:', error);
            this.hideTypingIndicator();
            this.addMessage('Sorry, I couldn\'t connect to the service. Please try again.', 'ai');
        } finally {
            this.setProcessingState(false);
        }
    }

    async sendMessageToServer(message) {
        const response = await fetch('/ai-chat/message', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': this.getCsrfToken()
            },
            body: JSON.stringify({
                message: message,
                sessionId: this.sessionId
            })
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        return await response.json();
    }

    setProcessingState(processing) {
        this.isProcessing = processing;
        const input = document.getElementById('aiMessageInput');
        const button = document.querySelector('#aiChatForm button[type="submit"]');

        input.disabled = processing;
        button.disabled = processing;
    }

    getCsrfToken() {
        return document.querySelector('meta[name="_csrf"]')?.getAttribute('content') || '';
    }

    generateSessionId() {
        return 'session_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9);
    }

    showTypingIndicator() {
        const messagesContainer = document.getElementById('aiChatMessages');
        const typingDiv = document.createElement('div');
        typingDiv.id = 'typing-indicator';
        typingDiv.className = 'ai-message ai';
        typingDiv.innerHTML = `
            <div class="message-bubble">
                <div class="message-sender">AI Assistant</div>
                <div class="typing-dots">
                    <span></span>
                    <span></span>
                    <span></span>
                </div>
            </div>
        `;
        messagesContainer.appendChild(typingDiv);
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
    }

    hideTypingIndicator() {
        const typingIndicator = document.getElementById('typing-indicator');
        if (typingIndicator) {
            typingIndicator.remove();
        }
    }

    addMessage(text, sender) {
        const messagesContainer = document.getElementById('aiChatMessages');
        const messageDiv = document.createElement('div');
        messageDiv.className = `ai-message ${sender}`;

        const bubbleDiv = document.createElement('div');
        bubbleDiv.className = 'message-bubble';

        const senderLabel = document.createElement('div');
        senderLabel.className = 'message-sender';
        senderLabel.textContent = sender === 'user' ? 'You' : 'AI Assistant';

        const messageText = document.createElement('div');
        messageText.textContent = text;

        bubbleDiv.appendChild(senderLabel);
        bubbleDiv.appendChild(messageText);
        messageDiv.appendChild(bubbleDiv);
        messagesContainer.appendChild(messageDiv);

        messagesContainer.scrollTop = messagesContainer.scrollHeight;
    }
}

function toggleAIChat() {
    const widget = document.getElementById('aiChatWidget');
    widget.classList.toggle('ai-chat-visible');
}

document.addEventListener('DOMContentLoaded', () => {
    new AIChatWidget();
});
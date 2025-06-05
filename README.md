# YouTube Analytics System

A real-time YouTube analytics system that processes video data through Kafka and provides analytics through a web interface and Telegram notifications.

## System Architecture

The system consists of four main components:

1. **Producer Service** (Port 8080)
   - Fetches YouTube video data using the YouTube API
   - Processes and sends data to Kafka topics
   - Handles video addition and updates

2. **Consumer Service** (Port 8081)
   - Processes data from Kafka
   - Provides real-time analytics
   - Serves the web dashboard

3. **Telegram Bot Service** 
   - Sends notifications to Telegram
   - Processes video updates with odd-length comments

4. **Infrastructure**
   - Kafka for message streaming
   - In-memory storage for analytics

## Prerequisites

- Docker and Docker Compose
- YouTube API Key
- Telegram Bot Token and Chat ID

## Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd <repository-directory>
   ```

2. **Environment Configuration**
   
   Create a `.env` file in the root directory with the following variables:
   ```env
   YOUTUBE_API_KEY=your_youtube_api_key
   TELEGRAM_BOT_TOKEN=your_telegram_bot_token
   TELEGRAM_BOT_USERNAME=your_bot_username
   TELEGRAM_CHAT_ID=your_chat_id
   ```

3. **Build and Start Services**
   ```bash
   docker-compose up -d
   ```

   This will start:
   - Kafka (accessible at kafka:9092)
   - Producer Service (Port 8080)
   - Consumer Service (Port 8081)
   - Telegram Bot Service (Port 8082)

4. **Verify Services**
   
   Check if all services are running:
   ```bash
   docker-compose ps
   ```

## Using the System

### Web Dashboard

1. Access the analytics dashboard at:
   ```
   http://localhost:8081
   ```

   The dashboard shows:
   - Total statistics (views, likes, comments)
   - Top performing videos
   - Latest video updates
   - Engagement metrics

### Adding Videos

1. Use the Producer spring boot webpage to add videos:
    - Paste a youtube video link in the text field 
    - Click the "Add video" button
   

### Telegram Notifications

The system will automatically:
- Send notifications for videos with odd-length comments
- Include video statistics and engagement metrics
- Provide direct links to videos

## Data Processing Rules

1. **Comment Processing**
   - Even-length comments → Analytics processing
   - Odd-length comments → Telegram notifications

2. **Analytics Calculation**
   - Engagement Rate = (Likes + Comments) / Views × 100
   - All metrics are updated in real-time
   - Top performers are calculated across all tracked videos

## Monitoring and Maintenance

### Logs
View service logs:
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f [service-name]
```

### Stopping the System
```bash
docker-compose down
```

To remove all data:
```bash
docker-compose down -v
```

## Troubleshooting

1. **Service Not Starting**
   - Check environment variables in `.env`
   - Verify Docker service is running
   - Check port conflicts

2. **No Data in Dashboard**
   - Verify Kafka is running
   - Check Producer service logs
   - Ensure valid YouTube API key

3. **No Telegram Notifications**
   - Verify bot token and chat ID
   - Check Telegram Bot service logs
   - Ensure bot is added to the chat

## API Endpoints

### Producer Service (8080)
- `GET /api/youtube/search?query=VIDEO_URL` - Add/update video
- `GET /api/youtube/videos` - List all tracked videos
- `DELETE /api/youtube/videos/{videoId}` - Remove video

### Consumer Service (8081)
- `GET /api/analytics/highest` - Get top performers
- `GET /api/analytics/videos` - Get all videos
- `GET /api/analytics/comparison` - Get comparative analytics


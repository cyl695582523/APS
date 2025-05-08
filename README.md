# ACPB APS (Airport Cabin Pickup Booking System)

## Project Overview
ACPB APS is a Spring Boot-based application for managing airport cabin pickup and booking services. The system handles various aspects of airport operations including cabin pickup, drop-off, booking management, and flow control.

## Project Structure
```
acpb-aps/
├── acpb-aps-flow-master/        # Main application module
├── acpb-aps-flow-unexpect/      # Unexpected flow handling
├── acpb-aps-flow-other/         # Other flow types
├── acpb-aps-flow-cabin-other/   # Cabin-related operations
├── acpb-aps-flow-exit/          # Exit flow management
├── acpb-aps-flow-cabin-pick-up/ # Cabin pickup operations
├── acpb-aps-flow-cabin-drop-off/# Cabin drop-off operations
├── acpb-aps-flow-entry/         # Entry flow management
├── acpb-aps-flow-booking/       # Booking management
├── acpb-aps-feign-client/       # Feign client configurations
├── acpb-aps-bom/                # Bill of Materials
└── common-framework/            # Common framework components
```

## Prerequisites
- Java 11 or higher
- Maven 3.6.3 or higher
- MySQL Database
- Git

## Building the Project

1. Clone the repository:
```bash
git clone https://github.com/cyl695582523/APS.git
```

2. Navigate to the project directory:
```bash
cd APS
```

3. Build the project:
```bash
mvn clean package
```

The executable JAR will be generated in the `acpb-aps-flow-master/target` directory.

## Running the Application

1. Navigate to the target directory:
```bash
cd acpb-aps-flow-master/target
```

2. Run the application:
```bash
java -jar acpb-aps-20250401-01-exec.jar
```

## Configuration

The application uses different configuration files for different environments:

- `application-dev.properties`: Development environment
- `application-prod.properties`: Production environment

Key configurations include:
- Database connection settings
- API endpoints and keys
- Logging configurations

## Port Configuration

The application runs on port 8080 by default. To change the port:
1. Open the appropriate properties file
2. Add or modify: `server.port=your_port_number`

## Logging

Logs are stored in the following locations:
- Development: `acpb-aps-dev.log`
- Production: `/var/log/acpb-aps/app.log`

## API Endpoints

The application exposes various REST endpoints for:
- Cabin pickup/drop-off operations
- Booking management
- Flow control
- System status monitoring

## Troubleshooting

1. **Port Already in Use**
   - Find the process using port 8080:
     ```bash
     netstat -ano | findstr :8080
     ```
   - Kill the process:
     ```bash
     taskkill /PID <PID> /F
     ```

2. **Database Connection Issues**
   - Verify database credentials in properties file
   - Check database server status
   - Ensure network connectivity

3. **Application Startup Issues**
   - Check logs for error messages
   - Verify all required environment variables are set
   - Ensure all dependencies are properly configured

## Contributing

1. Create a new branch for your feature
2. Make your changes
3. Submit a pull request

## License

[Add your license information here]

## Contact

[Add your contact information here] 
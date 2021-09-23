import com.techreturners.bubbleteaordersystem.model.*;
import com.techreturners.bubbleteaordersystem.service.BubbleTeaMessenger;
import com.techreturners.bubbleteaordersystem.service.BubbleTeaOrderService;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import testhelper.DummySimpleLogger;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class BubbleTeaOrderServiceTest {

    private PaymentDetails paymentDetails;
    private BubbleTeaMessenger mockMessenger;
    private BubbleTeaOrderService bubbleTeaOrderService;
    private final String name;
    private final String address;
    private final String debitCard;
    private final BubbleTeaTypeEnum bubbleTeaType;

    public BubbleTeaOrderServiceTest(String name,
                                     String address,
                                     String debitCard,
                                     BubbleTeaTypeEnum bubbleTeaType) {
        this.name = name;
        this.address = address;
        this.debitCard = debitCard;
        this.bubbleTeaType = bubbleTeaType;
    }

    @BeforeEach
    public void setup() {
        DebitCard testDebitCard = new DebitCard(debitCard);
        paymentDetails = new PaymentDetails(name, address, testDebitCard);
        DummySimpleLogger dummySimpleLogger = new DummySimpleLogger();
        mockMessenger = mock(BubbleTeaMessenger.class);
        bubbleTeaOrderService = new BubbleTeaOrderService(dummySimpleLogger, mockMessenger);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"hello kitty", "sanrio puroland", "0123456789", BubbleTeaTypeEnum.MatchaMilkTea},
                {"hello kitty1", "sanrio puroland1", "0173456786", BubbleTeaTypeEnum.PeachIceTea},
                {"hello kitty2", "sanrio puroland2", "0183456784", BubbleTeaTypeEnum.LycheeIceTea},
                {"hello kitty3", "sanrio puroland3", "0133456781", BubbleTeaTypeEnum.JasmineMilkTea},
                {"hello kitty4", "sanrio puroland4", "0143456785", BubbleTeaTypeEnum.OolongMilkTea},
        });
    }

    @Test
    public void shouldCreateBubbleTeaOrderRequestWhenCreateOrderRequestIsCalled() {

        //Arrange
        BubbleTea bubbleTea = new BubbleTea(BubbleTeaTypeEnum.OolongMilkTea, 4.5);
        BubbleTeaRequest bubbleTeaRequest = new BubbleTeaRequest(paymentDetails, bubbleTea);

        BubbleTeaOrderRequest expectedResult = new BubbleTeaOrderRequest(
                name,
                address,
                debitCard,
                bubbleTeaType
        );

        //Act
        BubbleTeaOrderRequest result = bubbleTeaOrderService.createOrderRequest(bubbleTeaRequest);
//        System.out.println(result);

        //Assert
        assertEquals(expectedResult.getName(), result.getName());
        assertEquals(expectedResult.getAddress(), result.getAddress());
        assertEquals(expectedResult.getDebitCardDigits(), result.getDebitCardDigits());
        assertEquals(expectedResult.getBubbleTeaType(), result.getBubbleTeaType());

        //Verify Mock was called with the BubbleTeaOrderRequest result object
        verify(mockMessenger).sendBubbleTeaOrderRequestEmail(result);
        verify(mockMessenger, times(1)).sendBubbleTeaOrderRequestEmail(result);
    }

}
